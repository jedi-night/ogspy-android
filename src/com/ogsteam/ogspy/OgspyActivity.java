package com.ogsteam.ogspy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.DatabasePreferencesHandler;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.fragments.tabs.TabsFragmentActivity;
import com.ogsteam.ogspy.network.ConnectionDetector;
import com.ogsteam.ogspy.network.DownloadTask;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.permission.ServerUtilities;
import com.ogsteam.ogspy.preferences.Accounts;
import com.ogsteam.ogspy.preferences.Preferences;
import com.ogsteam.ogspy.utils.OgspyUtils;

import java.util.Timer;
import java.util.TimerTask;

import static com.ogsteam.ogspy.permission.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.ogsteam.ogspy.permission.CommonUtilities.EXTRA_MESSAGE;
import static com.ogsteam.ogspy.permission.CommonUtilities.SENDER_ID;

public class OgspyActivity extends TabsFragmentActivity {
	public static final String DEBUG_TAG = OgspyActivity.class.getSimpleName();;
	public static int timer; // MIN * 60 * 1000 : minutes in seconds then milliseconds
	public Timer autoUpdateHostiles;
    protected String regId;
    // Variables
	public static DatabaseAccountHandler handlerAccount;
	public DatabasePreferencesHandler handlerPrefs;
	public static NotificationProvider notifProvider;
	public DownloadTask downloadHostilesTask;
	public static CommonUtilities commonUtilities;

	// Connection detector
    public static ConnectionDetector connection;
    
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
		// Step 1: Inflate layout
        setContentView(R.layout.ogspy_tab_host);
        // Step 2: Setup TabHost
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
                
        connection = new ConnectionDetector(getApplicationContext());
 
        // Check if Internet present
        if (!connection.isConnectingToInternet()) {
        	Toast.makeText(this, "Connexion internet inexistante", Toast.LENGTH_LONG).show();	
            // stop executing code by return
            return;
        }

		handlerAccount = new DatabaseAccountHandler(this);
		handlerPrefs = new DatabasePreferencesHandler(this);
		commonUtilities = new CommonUtilities(this);
		notifProvider = new NotificationProvider(this);
		downloadHostilesTask = new DownloadTask(this);
		timer=OgspyUtils.getTimerHostiles(this, handlerPrefs);
		setAutomaticCheckHostiles();
		
        // Make sure the device has the proper dependencies.
		try {
			GCMRegistrar.checkDevice(this);
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Probleme lors du checkdevice de GCM !");
		}
			
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
 
        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        
        // Get GCM registration id
        regId = GCMRegistrar.getRegistrationId(this);
                        
        // Check if regid already presents
        if (regId.equals("") && getFirstAccount() != null) {
            // Registration is not present, register now with GCM          
            GCMRegistrar.register(this, SENDER_ID);
        } else {
        	//GCMRegistrar.unregister(this);
            // Device is already registered on GCM
            if (!GCMRegistrar.isRegisteredOnServer(this)) {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                    	if(getFirstAccount() != null){
                    		ServerUtilities.register(context, getFirstAccount().getUsername(), regId);
                    	}
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
        }        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		/*case R.id.ogspy_activity:
			//setContentView(R.layout.hostiles);
			// Step 1: Inflate layout
	        setContentView(R.layout.ogspy_tab_host);
	        mTabHost.setCurrentTab(0); //set the tab as per the saved state
			return true;
		case R.id.account:
			Accounts.showAccount(this);
			return true;
		case R.id.prefs:
			Preferences.showPrefs(this);
			return true;*/
		case R.id.quit:
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//notifProvider.deleteNotificationHostile();
		//updateOgspyDatas();
	}

	@Override
	public void onPause() {
		//autoUpdate.cancel(); cancel the  
		super.onPause();
	}

	public void setAutomaticCheckHostiles(){
		if(timer > 0){
			autoUpdateHostiles = new Timer();
			autoUpdateHostiles.schedule(new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						public void run() {
							updateOgspyDatas();
						}
					});
				}
			}, 0, timer); // updates each timer secs
		}
	}

	private void updateOgspyDatas(){
        // Check if Internet present
        if (!connection.isConnectingToInternet()) {
        	Toast.makeText(this, "Connexion internet inexistante", Toast.LENGTH_LONG).show();	
            // stop executing code by return
            return;
        }         
		downloadHostilesTask = new DownloadTask(this);
		downloadHostilesTask.execute(new String[] { "do"});
	}

	public void saveAccount(View view){
		Accounts.saveAccount(view, this);
	}

	public void savePrefs(View view){
		Preferences.savePrefs(view, this);
	}

    public void unregisteringOgspy(View view){
        if(getFirstAccount() != null){
            if(GCMRegistrar.isRegisteredOnServer(this) && !regId.equals("")) {
                ServerUtilities.unregister(this, getFirstAccount().getUsername(), regId);
            }
        } else {
            CommonUtilities.displayMessage(this,"Votre compte n'est pas encore configuré !");
        }
    }

	public DatabaseAccountHandler getHandlerAccount() {
		return handlerAccount;
	}

	public static Account getFirstAccount() {
		Account account = null;
		if(handlerAccount.getAllAccounts() != null && handlerAccount.getAllAccounts().size() > 0){
			account = handlerAccount.getAllAccounts().get(0);
		}
		return account;
	}
	
	public DatabasePreferencesHandler getHandlerPrefs() {
		return handlerPrefs;
	}

	public NotificationProvider getNotifProvider() {
		return notifProvider;
	}

	public static void setTimer(int timer) {
		OgspyActivity.timer = timer;
	}	
	
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            //WakeLocker.acquire(getApplicationContext());
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My wakelook");
            // This will make the screen and power stay on
            // This will release the wakelook after 2000 ms
            wakeLock.acquire(2000);
            
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
            //lblMessage.append(newMessage + "\n");          
            //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            wakeLock.release();
        }
    };
     
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
}
