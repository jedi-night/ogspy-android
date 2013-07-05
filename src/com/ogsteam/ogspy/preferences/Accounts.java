package com.ogsteam.ogspy.preferences;

import android.widget.Toast;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.fragments.tabs.AccountFragment;

public class Accounts {

	/** Display the account windows */
	public static void showAccount(OgspyActivity activity, AccountFragment fragment) {
		//activity.setContentView(R.layout.accounts);
		if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
			Account account = activity.getHandlerAccount().getAccountById(0); 
			if(account != null){
				if(account.getUsername() != null && account.getUsername().length() > 0 && fragment.getUser() != null){
                    fragment.getUser().setText(account.getUsername());
				}
				if(account.getPassword()!=null && account.getPassword().length() > 0 && fragment.getPassword() != null){
                    fragment.getPassword().setText(account.getPassword());
				}
				if(account.getServerUrl()!=null && account.getServerUrl().length() > 0 && fragment.getServerUrl() != null){
                    fragment.getServerUrl().setText(account.getServerUrl());
				}
				if(account.getServerUnivers()!=null && account.getServerUnivers().length() > 0 && fragment.getServerUniverse() != null){
                    fragment.getServerUniverse().setText(account.getServerUnivers());
				}
			}
		}
	}
	
	/** Called when the user clicks the Save button in account*/
	public static void saveAccount(OgspyActivity activity) {
		String username = AccountFragment.getUser().getText().toString();
		String password = AccountFragment.getPassword().getText().toString();
		String serverUrl = AccountFragment.getServerUrl().getText().toString();
		String serverUnivers = AccountFragment.getServerUniverse().getText().toString();
		
		if(check(activity, username, password, serverUrl, serverUnivers)) {
			if(activity.getHandlerAccount().getAccountsCount() > 0){
				activity.getHandlerAccount().deleteAllAccounts();
			}
			if(activity.getHandlerAccount().addAccount(new Account(0,username, password, serverUrl, serverUnivers)) != -1){
				Toast.makeText(activity, activity.getString(R.string.save_account_ok), Toast.LENGTH_LONG).show();	
			} else {
				Toast.makeText(activity, activity.getString(R.string.save_account_ko), Toast.LENGTH_LONG).show();
			}
		}
	}

	private static boolean check(OgspyActivity activity, String username, String password, String serverUrl, String serverUnivers) {
		boolean status = true;
		StringBuilder sb = new StringBuilder();
		
		if(username == null || username.length() == 0){
			status = false;
			sb.append(activity.getString(R.string.save_account_username_ko));			
		}
		if(password == null || password.length() == 0){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(activity.getString(R.string.save_account_password_ko));			
		}
		if(!serverUrl.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(activity.getString(R.string.save_account_server_url_ko));
		}
		// Check server univers
		if(!serverUnivers.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");	
			}
			sb.append(activity.getString(R.string.save_account_server_univers_ko));
		}
		if(sb.toString().length() > 0){
			Toast.makeText(activity, sb.toString(), Toast.LENGTH_LONG).show();
		}
		return status;
	}

}
