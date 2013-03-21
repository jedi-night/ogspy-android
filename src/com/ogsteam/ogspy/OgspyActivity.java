package com.ogsteam.ogspy;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.utils.Constants;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.MD5;
import com.ogsteam.ogspy.utils.SHA1;

import java.util.Timer;
import java.util.TimerTask;

public class OgspyActivity extends Activity {
	private static final String DEBUG_TAG = OgspyActivity.class.getSimpleName();;
	private static final int timer = 5 * 60 * 1000; // MIN * 60 * 1000 : minutes in seconds then milliseconds
    private Timer autoUpdate;

	// Variables
	private DatabaseAccountHandler handler;
	protected static String dataFromAsyncTask; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new DatabaseAccountHandler(this);
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
	            showSettings();
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
	  autoUpdate.cancel();
	  super.onPause();
	 }
	 
	 private void updateOgspyDatas(){
		 new DownloadTask(this).execute(new String[] { "do"});
	 }
	 
	/** Display the settings windows */
	public void showSettings() {		
		setContentView(R.layout.settings);
		if(!handler.getAllAccounts().isEmpty()){
			Account account = handler.getAccountById(0); 
			if(account != null){
				if(account.getUsername()!=null && account.getUsername().length() > 0){
					((EditText) findViewById(R.id.ogspy_user)).setText(account.getUsername());
				}
				if(account.getPassword()!=null && account.getPassword().length() > 0){
					((EditText) findViewById(R.id.ogspy_password)).setText(account.getPassword());
				}
				if(account.getServerUrl()!=null && account.getServerUrl().length() > 0){
					((EditText) findViewById(R.id.ogspy_server_url)).setText(account.getServerUrl());
				}
				if(account.getServerUnivers()!=null && account.getServerUnivers().length() > 0){
					((EditText) findViewById(R.id.ogspy_server_universe)).setText(account.getServerUnivers());
				}
			}
		}
	}
	
	/** Called when the user clicks the Save button in Settings*/
	public void saveSettings(View view) {
		String username = ((EditText) findViewById(R.id.ogspy_user)).getText().toString();
		String password = ((EditText) findViewById(R.id.ogspy_password)).getText().toString();
		String serverUrl = ((EditText) findViewById(R.id.ogspy_server_url)).getText().toString();
		String serverUnivers = ((EditText) findViewById(R.id.ogspy_server_universe)).getText().toString();
		
		if(checkSettings(username, password, serverUrl, serverUnivers)) {
			if(handler.getAccountsCount() > 0){
				handler.deleteAllAccounts();
			}
			if(handler.addAccount(new Account(0,username, password, serverUrl, serverUnivers)) != -1){
				Toast.makeText(this, this.getString(R.string.save_settings_ok), Toast.LENGTH_SHORT).show();	
			} else {
				Toast.makeText(this, this.getString(R.string.save_settings_ko), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean checkSettings(String username, String password, String serverUrl, String serverUnivers) {
		boolean status = true;
		StringBuilder sb = new StringBuilder();
		
		if(username == null || username.length() == 0){
			status = false;
			sb.append(this.getString(R.string.save_settings_username_ko));			
		}
		if(password == null || password.length() == 0){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(this.getString(R.string.save_settings_password_ko));			
		}
		if(!serverUrl.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(this.getString(R.string.save_settings_server_url_ko));
		}
		// Check server univers
		if(!serverUnivers.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(this.getString(R.string.save_settings_server_univers_ko));
		}
		if(sb.toString().length() > 0){
			Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
		}
		return status;
	}

	private static String enryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return MD5.toMD5(SHA1.toSHA1(password));
	}
	
	private String traiterReponseHostiles(String data){
		// ({"new_messages": 0,"type": "checkhostiles","check": 0,"user": "","execution": 14.19})
		String donnees = data.trim().substring(2, (data.trim().length()-3)); // suppress ({ and })
		String[] strArray = donnees.split(",");
		for(int i = 0; i < strArray.length; i++){
			String[] tabData = strArray[i].split(":");
			if(tabData[0].replaceAll("\"","").equals("check")){
				if(tabData[1].trim().equals("1")){
					return "Flottes hostiles en approche !";
				} else {
					return "Aucune flotte hostiles en approche.";
				}
			}
			//Log.i(DEBUG_TAG, tabData[0]+"="+tabData[1]);
		}
		return "Aucune information n'a pu être récupérée";
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {
	    private Activity activity;

	    public DownloadTask(Activity activity) {
	        this.activity = activity;
	    }

	    @Override
		protected String doInBackground(String... params) {
			try {
				if(!handler.getAllAccounts().isEmpty()){
					Account account = handler.getAccountById(0);
					String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), enryptPassword(account.getPassword()), account.getServerUnivers());
					dataFromAsyncTask = traiterReponseHostiles(HttpUtils.downloadUrl(url));
					//traiterReponseHostiles(dataFromAsyncTask);
							//.match(/\(\{.*\}\)/)));
				}
			} catch (Exception e) {
				Log.e(DEBUG_TAG, "Problème lors du telechargement !",e);
			}
			return null;
		}

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

		protected void onPostExecute(String result) {
	    	((EditText) findViewById(R.id.response_ogspy)).setText(dataFromAsyncTask);    	
		}

	}
	
}
