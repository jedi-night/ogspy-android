package com.ogsteam.ogspy;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.DatabasePreferencesHandler;
import com.ogsteam.ogspy.fragments.tabs.TabsFragmentActivity;
import com.ogsteam.ogspy.network.DownloadTask;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.preferences.Accounts;
import com.ogsteam.ogspy.preferences.Preferences;
import com.ogsteam.ogspy.utils.OgspyUtils;

public class OgspyActivity extends TabsFragmentActivity {
	public static final String DEBUG_TAG = OgspyActivity.class.getSimpleName();;
	public static int timer; // MIN * 60 * 1000 : minutes in seconds then milliseconds
	public Timer autoUpdateHostiles;

	// Variables
	public DatabaseAccountHandler handlerAccount;
	public DatabasePreferencesHandler handlerPrefs;
	public NotificationProvider notifProvider;
	public DownloadTask downloadTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Step 1: Inflate layout
        setContentView(R.layout.ogspy_tab_host);
        // Step 2: Setup TabHost
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
		handlerAccount = new DatabaseAccountHandler(this);
		handlerPrefs = new DatabasePreferencesHandler(this);
		notifProvider = new NotificationProvider(this);
		downloadTask = new DownloadTask(this);
		timer=OgspyUtils.getTimerHostiles(this, handlerPrefs);
		setAutomaticCheckHostiles();
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
		updateOgspyDatas();
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
		downloadTask = new DownloadTask(this);
		downloadTask.execute(new String[] { "do"});
	}

	public void saveAccount(View view){
		Accounts.saveAccount(view, this);
	}

	public void savePrefs(View view){
		Preferences.savePrefs(view, this);
	}


	public DatabaseAccountHandler getHandlerAccount() {
		return handlerAccount;
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
}
