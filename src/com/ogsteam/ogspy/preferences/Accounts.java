package com.ogsteam.ogspy.preferences;

import android.content.SharedPreferences;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.utils.helpers.Constants;

public class Accounts {

    public static void showAccount(OgspyActivity activity, SharedPreferences preferences) {
        if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
            Account account = activity.getHandlerAccount().getAccountById(0);
            if(account != null){
                SharedPreferences.Editor editor = preferences.edit();
                if(account.getUsername() != null && account.getUsername().length() > 0 && !preferences.contains("login")){
                    editor.putString("login",account.getUsername());
                    editor.commit();
                }
                if(account.getPassword()!=null && account.getPassword().length() > 0 && !preferences.contains("password")){
                    editor.putString("password",account.getPassword());
                    editor.commit();
                }
                if(account.getServerUrl()!=null && account.getServerUrl().length() > 0 && !preferences.contains("serverUrl")){
                    editor.putString("serverUrl",account.getServerUrl());
                    editor.commit();
                }
                if(account.getServerUnivers()!=null && account.getServerUnivers().length() > 0 && !preferences.contains("serverUnivers")){
                    editor.putString("serverUnivers",account.getServerUnivers());
                    editor.commit();
                }
            }
        }
    }

	/** Called when the user clicks the Save button in account*/
	/*public static void saveAccount(OgspyActivity activity) {
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
	}*/

    public static void saveAccount(OgspyActivity activity,SharedPreferences preferences) throws OgspyException {

        String username = preferences.getString("login","");
        String password = preferences.getString("password", "");
        String serverUrl = preferences.getString("serverUrl", "");
        String serverUnivers = preferences.getString("serverUnivers", "");

        if(check(activity, username, password, serverUrl, serverUnivers)) {
            if(activity.getHandlerAccount().getAccountsCount() > 0){
                activity.getHandlerAccount().deleteAllAccounts();
            }
            if(activity.getHandlerAccount().addAccount(new Account(0,username, password, serverUrl, serverUnivers)) != -1){
                //CommonUtilities.displayMessage(activity, activity.getString(R.string.save_account_ok));
                //Toast.makeText(activity, activity.getString(R.string.save_account_ok), Toast.LENGTH_LONG).show();
            } else {
                new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être sauvegardé", Constants.EXCEPTION_DATA_SAVE));
                //CommonUtilities.displayMessage(activity, activity.getString(R.string.save_account_ko));
                //Toast.makeText(activity, activity.getString(R.string.save_account_ko), Toast.LENGTH_LONG).show();
            }
        }
    }

	private static boolean check(OgspyActivity activity, String username, String password, String serverUrl, String serverUnivers) throws OgspyException {
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
		if(serverUrl == null || !serverUrl.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");
			}
			sb.append(activity.getString(R.string.save_account_server_url_ko));
		}
		// Check server univers
		if(serverUnivers == null || !serverUnivers.matches("http://.*")){
			status = false;
			if(sb.toString().length() > 0) {
				sb.append("\n");
			}
			sb.append(activity.getString(R.string.save_account_server_univers_ko));
		}
		if(sb.toString().length() > 0){
            throw new OgspyException(sb.toString(), Constants.EXCEPTION_SAISIE);
		}
		return status;
	}
}
