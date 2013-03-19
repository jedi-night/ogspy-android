package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.models.Account;

public class OgspyActivity extends Activity {
	private DatabaseAccountHandler handler;
	
	
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
		Account account = handler.getAccountById(0); 
		if(account != null){
			((EditText) findViewById(R.id.ogspy_user)).setText(account.getUsername());
			((EditText) findViewById(R.id.ogspy_password)).setText(account.getPassword());
			((EditText) findViewById(R.id.ogspy_server_url)).setText(account.getServerUrl());
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
}
