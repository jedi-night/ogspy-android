package com.ogsteam.ogspy;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.network.DownloadTask;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.preferences.Settings;

public class OgspyActivity extends Activity {
	public static final String DEBUG_TAG = OgspyActivity.class.getSimpleName();;
	private static final int timer = 10 * 60 * 1000; // MIN * 60 * 1000 : minutes in seconds then milliseconds
    private Timer autoUpdate;

	// Variables
	public DatabaseAccountHandler handler;
	public NotificationProvider notifProvider;
	//protected static String dataFromAsyncTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new DatabaseAccountHandler(this);
		notifProvider=new NotificationProvider(this);
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
	        case R.id.ogspy_activity:
	        	setContentView(R.layout.activity_main);
	            return true;
	        case R.id.settings:
	            Settings.showSettings(this);
	            return true;
	        case R.id.quit:
	        	this.finish();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	 @Override
	 public void onResume() {
	  super.onResume();
	  notifProvider.deleteNotificationHostile();
	  autoUpdate = new Timer();
	  autoUpdate.schedule(new TimerTask() {
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

	 @Override
	 public void onPause() {
	  //autoUpdate.cancel();
	  super.onPause();
	 }
	 
	 private void updateOgspyDatas(){
		 new DownloadTask(this).execute(new String[] { "do"});
	 }
	 
	 public void saveSettings(View view){
		 Settings.saveSettings(view, this);
	 }
	 
	public DatabaseAccountHandler getHandler() {
		return handler;
	}

	public NotificationProvider getNotifProvider() {
		return notifProvider;
	}
}
