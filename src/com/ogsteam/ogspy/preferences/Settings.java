package com.ogsteam.ogspy.preferences;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;

public class Settings {

	/** Display the settings windows */
	public static void showSettings(OgspyActivity activity) {		
		activity.setContentView(R.layout.settings);
		if(!activity.getHandler().getAllAccounts().isEmpty()){
			Account account = activity.getHandler().getAccountById(0); 
			if(account != null){
				if(account.getUsername()!=null && account.getUsername().length() > 0){
					((EditText) activity.findViewById(R.id.ogspy_user)).setText(account.getUsername());
				}
				if(account.getPassword()!=null && account.getPassword().length() > 0){
					((EditText) activity.findViewById(R.id.ogspy_password)).setText(account.getPassword());
				}
				if(account.getServerUrl()!=null && account.getServerUrl().length() > 0){
					((EditText) activity.findViewById(R.id.ogspy_server_url)).setText(account.getServerUrl());
				}
				if(account.getServerUnivers()!=null && account.getServerUnivers().length() > 0){
					((EditText) activity.findViewById(R.id.ogspy_server_universe)).setText(account.getServerUnivers());
				}
			}
		}
	}
	
	/** Called when the user clicks the Save button in Settings*/
	public static void saveSettings(View view, OgspyActivity activity) {
		String username = ((EditText) activity.findViewById(R.id.ogspy_user)).getText().toString();
		String password = ((EditText) activity.findViewById(R.id.ogspy_password)).getText().toString();
		String serverUrl = ((EditText) activity.findViewById(R.id.ogspy_server_url)).getText().toString();
		String serverUnivers = ((EditText) activity.findViewById(R.id.ogspy_server_universe)).getText().toString();
		
		if(checkSettings(activity, username, password, serverUrl, serverUnivers)) {
			if(activity.getHandler().getAccountsCount() > 0){
				activity.getHandler().deleteAllAccounts();
			}
			if(activity.getHandler().addAccount(new Account(0,username, password, serverUrl, serverUnivers)) != -1){
				Toast.makeText(activity, activity.getString(R.string.save_settings_ok), Toast.LENGTH_LONG).show();	
			} else {
				Toast.makeText(activity, activity.getString(R.string.save_settings_ko), Toast.LENGTH_LONG).show();
			}
		}
	}

	private static boolean checkSettings(OgspyActivity activity, String username, String password, String serverUrl, String serverUnivers) {
		boolean status = true;
		StringBuilder sb = new StringBuilder();
		
		if(username == null || username.length() == 0){
			status = false;
			sb.append(activity.getString(R.string.save_settings_username_ko));			
		}
		if(password == null || password.length() == 0){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(activity.getString(R.string.save_settings_password_ko));			
		}
		if(!serverUrl.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(activity.getString(R.string.save_settings_server_url_ko));
		}
		// Check server univers
		if(!serverUnivers.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(activity.getString(R.string.save_settings_server_univers_ko));
		}
		if(sb.toString().length() > 0){
			Toast.makeText(activity, sb.toString(), Toast.LENGTH_LONG).show();
		}
		return status;
	}

}
