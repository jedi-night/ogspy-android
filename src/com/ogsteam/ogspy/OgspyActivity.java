package com.ogsteam.ogspy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.DatabaseMessagesHandler;
import com.ogsteam.ogspy.data.DatabasePreferencesHandler;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.fragments.tabs.GeneralFragment;
import com.ogsteam.ogspy.fragments.tabs.TabsFragmentActivity;
import com.ogsteam.ogspy.network.ConnectionDetector;
import com.ogsteam.ogspy.network.download.DownloadAllianceTask;
import com.ogsteam.ogspy.network.download.DownloadHostilesTask;
import com.ogsteam.ogspy.network.download.DownloadRentabilitesTask;
import com.ogsteam.ogspy.network.download.DownloadServerTask;
import com.ogsteam.ogspy.network.download.DownloadSpysTask;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.permission.ServerUtilities;
import com.ogsteam.ogspy.sliding.menu.adapter.NavDrawerListAdapter;
import com.ogsteam.ogspy.sliding.menu.model.NavDrawerItem;
import com.ogsteam.ogspy.ui.DialogHandler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.ogsteam.ogspy.permission.CommonUtilities.SENDER_ID;

public class OgspyActivity extends TabsFragmentActivity {

    /**
     * Receiving push messages
     * */
    /*private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
                // Waking up mobile if it is sleeping
                //WakeLocker.acquire(getApplicationContext());
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "OGSpy wakelock");
                // This will make the screen and power stay on
                // This will release the wakelook after 2000 ms
                wakeLock.acquire(5000);

                // Showing received message
                //lblMessage.append(newMessage + "\n");
                //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

                // Releasing wake lock
                if(wakeLock!= null && wakeLock.isHeld()) {
                    wakeLock.release();
                }
            } catch (Exception e) {
                Log.e(DEBUG_TAG,"Problème avec le wakelock OGSpy !",e);
            }
        }
    };
*/
    public static OgspyActivity activity;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    public final String versionAndroid = Build.VERSION.RELEASE;
    public static String versionOgspy = "";
    public final String deviceName = retrieveDeviceName();

    public static final String DEBUG_TAG = OgspyActivity.class.getSimpleName();
	public static int timer; // MIN * 60 * 1000 : minutes in seconds then milliseconds
	public Timer autoUpdateHostiles;
    protected String regId;
    private static boolean isWaiting = false;
    private static boolean isConnectionProblem = false;

    // Variables
	public static DatabaseAccountHandler handlerAccount;
	public static DatabasePreferencesHandler handlerPrefs;
    public static DatabaseMessagesHandler handlerMessages;

	public static NotificationProvider notifProvider;
    public static CommonUtilities commonUtilities;

    public static DownloadServerTask downloadServerTask;
    public static DownloadHostilesTask downloadHostilesTask;
    public static DownloadAllianceTask downloadAllianceTask;
    public static DownloadSpysTask downloadSpysTask;
    public static DownloadRentabilitesTask downloadRentasTask;

	// Connection detector
    public static ConnectionDetector connection;
    
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
		super.onCreate(savedInstanceState);

        activity = this;

        try{
            versionOgspy = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.e(DEBUG_TAG,"Impossible de recuperer la version ogspy",e);
        }

		// Step 1: Inflate layout
        //setContentView(R.layout.ogspy_tab_host);
        // Step 2: Setup TabHost
        /*initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null && mTabHost != null && savedInstanceState.getString("tab") != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
          */
        connection = new ConnectionDetector(getApplicationContext());

		handlerAccount = new DatabaseAccountHandler(this);
		handlerPrefs = new DatabasePreferencesHandler(this);
        handlerMessages = new DatabaseMessagesHandler(this);

		commonUtilities = new CommonUtilities(this);
		notifProvider = new NotificationProvider(this);

		timer = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("timer_hostiles","0"));

        downloadServerTask = new DownloadServerTask(this);
        downloadAllianceTask = new DownloadAllianceTask(this);

        setAutomaticCheckHostiles();
        doGcm();

        setMenuSliding();

        downloadServerTask.execute(new String[]{"do"});
        downloadAllianceTask.execute(new String[]{"do"});

        // Check if Internet present
        if (!connection.isConnectingToInternet()) {
            //Toast.makeText(this, getString(R.string.connexion_ko), Toast.LENGTH_LONG).show();
            // stop executing code by return
            //return;
            showWaiting(false);
            showConnectivityProblem(true);
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
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
		switch (item.getItemId()) {
		/*case R.id.ogspy_activity:
			//setContentView(R.layout.hostiles);
			// Step 1: Inflate layout
	        setContentView(R.layout.ogspy_tab_host);
	        mTabHost.setCurrentTab(0); //set the tab as per the saved state
			return true;*/
		case R.id.about:
            startActivity(new Intent(this, OgspyAboutActivity.class));
			return true;
		case R.id.prefs:
            //startActivityForResult(new Intent(this, OgspyPreferencesActivity.class), CODE_RETOUR_PREFS);
            startActivity(new Intent(this, OgspyPreferencesActivity.class));
            return true;
		case R.id.quit:
			this.finish();
        }
		return super.onOptionsItemSelected(item);
	}

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.prefs).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
		super.onResume();
        doGcm();
		//notifProvider.deleteNotificationHostile();
		//updateOgspyDatas();
        setAutomaticCheckHostiles();
	}

	@Override
	public void onPause() {
        if(autoUpdateHostiles != null) autoUpdateHostiles.cancel();
		super.onPause();
	}

	public void setAutomaticCheckHostiles(){
		if(timer > 0){
			autoUpdateHostiles = new Timer();
            // Check if Internet present
            if(connection==null){ return;}
            if ( !connection.isConnectingToInternet()) {
                Toast.makeText(this, getString(R.string.connexion_ko), Toast.LENGTH_LONG).show();
                // stop executing code by return
                return;
            }

            downloadHostilesTask = new DownloadHostilesTask(this);

            downloadSpysTask = new DownloadSpysTask(this);

			autoUpdateHostiles.schedule(new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						public void run() {
                            if (downloadHostilesTask.getStatus().equals(AsyncTask.Status.FINISHED)){
                                downloadHostilesTask.execute(new String[] { "do"});
                            }
                            if (downloadSpysTask.getStatus().equals(AsyncTask.Status.FINISHED)){
                                downloadSpysTask.execute(new String[] { "do"});
                            }
						}
					});
				}
			}, 0, timer); // updates each timer secs
		}

	}

    public void unregisteringOgspy(View view){
        new DialogHandler().confirm(this,
                                    "Désincription aux notifications",
                                    "Etes-vous sûr de vouloir vous désinscrire des notifications du serveur OGSPY ?",
                                    "Non",
                                    "Oui",
                                    new Runnable() {
                                        public void run() {
                                            processUnregistering();
                                        }
                                    },
                                    new Runnable() {
                                        public void run() {
                                        }
                                    }
        );
    }

    public void processUnregistering(){
        if(getFirstAccount() != null){
            if(GCMRegistrar.isRegisteredOnServer(this) && !regId.equals("")) {
                ServerUtilities.unregister(this, getFirstAccount().getUsername(), regId);
                CommonUtilities.displayMessage(this,"Désinscription envoyée");
            } else {
                CommonUtilities.displayMessage(this,"Vous n'etes pas enregistré, désincription impossible");
            }
        } else {
            CommonUtilities.displayMessage(this,getString(R.string.account_not_configured));
        }
    }

	public static DatabaseAccountHandler getHandlerAccount() {
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

	public static NotificationProvider getNotifProvider() {
		return notifProvider;
	}

	public static void setTimer(int timer) {
		OgspyActivity.timer = timer;
	}	

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            //unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    private boolean doGcm(){
        boolean retour = false;
        // Make sure the device has the proper dependencies.
        try {
            GCMRegistrar.checkDevice(this);

            // Make sure the manifest was properly set - comment out this line
            // while developing the app, then uncomment it when it's ready.
            GCMRegistrar.checkManifest(this);

            //registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

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
                    final Account account = getFirstAccount();

                    /*mRegisterTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {*/
                            // Register on our server
                            // On server creates a new user
                            if(account != null){
                                ServerUtilities.register(context, account.getUsername(), regId);
                            }
                            /*return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            mRegisterTask = null;
                        }

                    };
                    mRegisterTask.execute(null, null, null);*/
                }
            }
            retour = true;
        } catch (Exception e) {
            Log.e(DEBUG_TAG, getString(R.string.gcm_problem));
            Log.e(DEBUG_TAG, e.getMessage());
            retour = false;
        }
        return retour;
    }

    public static String getVersionOgspy() {
        return versionOgspy;
    }

    public static DownloadHostilesTask getDownloadHostilesTask() {
        return downloadHostilesTask;
    }

    public static DownloadAllianceTask getDownloadAllianceTask() {
        return downloadAllianceTask;
    }

    public static DownloadSpysTask getDownloadSpysTask() {
        return downloadSpysTask;
    }

    public static DownloadRentabilitesTask getDownloadRentasTask() {
        return downloadRentasTask;
    }

    public void sendAlert(View v){
        if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
            EditText messageEditText = getFragmentAlert().getMessage();
            if (messageEditText.getText() != null && messageEditText.getText().length() > 0) {
                ServerUtilities.sendAlertMesage(this, regId, activity.getHandlerAccount().getAccountById(0).getUsername(), messageEditText.getText().toString());
                messageEditText.setText("");
            } else {
                CommonUtilities.displayMessage(this,"Le message d'alerte est vide, il n'a donc pas été envoyé.");
            }
        }
    }

    public void showWaiting(boolean visible){
            if(visible){
                if(!isWaiting){
                    findViewById(android.R.id.tabcontent).setVisibility(View.GONE);
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    isWaiting=true;
                }
            } else {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                findViewById(android.R.id.tabcontent).setVisibility(View.VISIBLE);
                isWaiting=false;
            }
    }

    private void setMenuSliding() {
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(this, navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        /*if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }*/
    }

    public void showConnectivityProblem(boolean visible) {
        if (visible) {
            pushFragments("connection", getFragmentConnection());
            isConnectionProblem = true;
        } else {
            pushFragments("noproblem", getLastFragment());
            isConnectionProblem = false;
        }
    }

    public String getRegId() {
        return regId;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String retrieveDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String buildNumber = Build.ID;
        if (model.startsWith(manufacturer)) {
            return capitalize(model) + "-" + buildNumber;
        } else {
            return capitalize(manufacturer) + "-" + model + "-" + buildNumber;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new GeneralFragment();
                break;
            case 1: // prefs
                startActivity(new Intent(this, OgspyPreferencesActivity.class));
                break;
            default:
                break;
        }

        if (fragment != null) {
            pushFragments(TAB_A, fragment);
        } else {
            // error in creating fragment
            Log.d(DEBUG_TAG, "Error in creating fragmentor not a fragment");
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
