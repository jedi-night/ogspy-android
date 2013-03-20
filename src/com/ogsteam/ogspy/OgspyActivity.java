package com.ogsteam.ogspy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

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
import com.ogsteam.ogspy.utils.StringUtils;

public class OgspyActivity extends Activity {
	private static final String DEBUG_TAG = "OgspyActivity";
	
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
	
	/** Display the settings windows */
	public void showSettings() {		
		setContentView(R.layout.settings);
		if(!handler.getAllAccounts().isEmpty()){
			Account account = handler.getAccountById(0); 
			if(account != null){
				((EditText) findViewById(R.id.ogspy_user)).setText(account.getUsername());
				((EditText) findViewById(R.id.ogspy_password)).setText(account.getPassword());
				((EditText) findViewById(R.id.ogspy_server_url)).setText(account.getServerUrl());
			}
		}
	}
	
	/** Called when the user clicks the Save button in Settings*/
	public void saveSettings(View view) {
		String username = ((EditText) findViewById(R.id.ogspy_user)).getText().toString();
		String password = ((EditText) findViewById(R.id.ogspy_password)).getText().toString();
		String serverUrl = ((EditText) findViewById(R.id.ogspy_server_url)).getText().toString();
		
		if(checkSettings(username, password, serverUrl)) {
			if(handler.getAccountsCount() > 0){
				handler.deleteAllAccounts();
			}
			if(handler.addAccount(new Account(0,username, password, serverUrl)) != -1){
				Toast.makeText(this, this.getString(R.string.save_settings_ok), Toast.LENGTH_SHORT).show();	
			} else {
				Toast.makeText(this, this.getString(R.string.save_settings_ko), Toast.LENGTH_SHORT).show();
			}
		}
		
		
		/*List<Account> accounts = handler.getAllAccounts();
		for (Account account : accounts) {
            String log = "Id: "+account.getId()+" ,Username: " + account.getUsername() + " ,Password: " + account.getPassword() + " ,Server: " + account.getServerUrl();
            // Writing Accounts to log
            Log.d("Account: ", log);
		}*/
		//Log.i(this.getLocalClassName(), new StringBuilder().append(userOgspy).append(";").append(password).append(";").append(serverUrl).toString());
	}

	private boolean checkSettings(String username, String password, String serverUrl) {
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
		if(sb.toString().length() > 0){
			Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
		}
		return status;
	}
	
	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    int len = 500;
	        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(DEBUG_TAG, "The response is: " + response);
	        is = conn.getInputStream();

	        // Convert the InputStream into a string
	        String contentAsString = readIt(is, len);
	        return contentAsString;
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
	
	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, String> {
	    private Activity activity;

	    public MyAsyncTask(Activity activity) {
	        this.activity = activity;
	    }

	    @Override
		protected String doInBackground(String... params) {
			try {
				if(!handler.getAllAccounts().isEmpty()){
					Account account = handler.getAccountById(0);
					String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getUsername(), account.getPassword());
					dataFromAsyncTask = downloadUrl(url);
				}
			} catch (Exception e) {
				Log.e(DEBUG_TAG, "Prioblème lors du telechargement");
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
