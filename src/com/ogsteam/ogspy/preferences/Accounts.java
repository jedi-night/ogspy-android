package com.ogsteam.ogspy.preferences;

import android.content.SharedPreferences;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;

public class Accounts {

    public static void showAccount(OgspyActivity activity, SharedPreferences preferences) {
        if (!activity.getHandlerAccount().getAllAccounts().isEmpty()) {
            String positionAccount = preferences.getString("prefs_accounts", null);
            Account account = null;
            if (positionAccount != null) {
                account = activity.getHandlerAccount().getAccountById(Integer.parseInt(positionAccount));
            } else {
                account = activity.getHandlerAccount().getAllAccounts().get(0);
            }
            if (account != null) {
                SharedPreferences.Editor editor = preferences.edit();
                int accountId = account.getId();
                String suffixe = (accountId > 0 ? positionAccount : "");
                if (account.getUsername() != null && account.getUsername().length() > 0 && !preferences.contains("login" + suffixe)) {
                    editor.putString("login" + suffixe, account.getUsername());
                    editor.commit();
                }
                if (account.getPassword() != null && account.getPassword().length() > 0 && !preferences.contains("password" + suffixe)) {
                    editor.putString("password" + suffixe, account.getPassword());
                    editor.commit();
                }
                if (account.getServerUrl() != null && account.getServerUrl().length() > 0 && !preferences.contains("serverUrl" + suffixe)) {
                    editor.putString("serverUrl" + suffixe, account.getServerUrl());
                    editor.commit();
                }
                if (account.getServerUnivers() != null && account.getServerUnivers().length() > 0 && !preferences.contains("serverUnivers" + suffixe)) {
                    editor.putString("serverUnivers" + suffixe, account.getServerUnivers());
                    editor.commit();
                }
            }
        }
    }

    /**
     * Called when the user clicks the Save button in account
     */
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
    public static void saveAccount(OgspyActivity activity, SharedPreferences preferences) throws OgspyException {

        String username = preferences.getString("login", "");
        String password = preferences.getString("password", "");
        String serverUrl = preferences.getString("serverUrl", "");
        String serverUnivers = preferences.getString("serverUnivers", "");

        if (check(activity, username, password, serverUrl, serverUnivers)) {
            if (activity.getHandlerAccount().getAccountsCount() > 0) {
                activity.getHandlerAccount().deleteAllAccounts();
            }
            if (activity.getHandlerAccount().addAccount(new Account(0, username, password, serverUrl, serverUnivers)) != -1) {
                //CommonUtilities.displayMessage(activity, activity.getString(R.string.save_account_ok));
                //Toast.makeText(activity, activity.getString(R.string.save_account_ok), Toast.LENGTH_LONG).show();
            } else {
                new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être sauvegardé", Constants.EXCEPTION_DATA_SAVE));
                //CommonUtilities.displayMessage(activity, activity.getString(R.string.save_account_ko));
                //Toast.makeText(activity, activity.getString(R.string.save_account_ko), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void saveAccount(OgspyActivity activity, String username, String password, String serverUrl, String serverUnivers) {
        Account lastAccount = activity.getHandlerAccount().getLastAccount();
        if (lastAccount != null) {
            if (activity.getHandlerAccount().addAccount(new Account((lastAccount.getId() + 1), username, password, serverUrl, serverUnivers)) != -1) {
                CommonUtilities.displayMessage(activity, StringUtils.formatPattern(activity.getString(R.string.save_account_ok), username));
            } else {
                new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être sauvegardé", Constants.EXCEPTION_DATA_SAVE));
            }
        } else {
            if (activity.getHandlerAccount().addAccount(new Account(0, username, password, serverUrl, serverUnivers)) != -1) {
                CommonUtilities.displayMessage(activity, StringUtils.formatPattern(activity.getString(R.string.save_account_ok), username));
            } else {
                new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être sauvegardé", Constants.EXCEPTION_DATA_SAVE));
            }
        }

    }


    public static void updateAccount(OgspyActivity activity, String id, String username, String password, String serverUrl, String serverUnivers) {
        if (activity.getHandlerAccount().updateAccount(new Account(Integer.parseInt(id), username, password, serverUrl, serverUnivers)) != -1) {
            CommonUtilities.displayMessage(activity, StringUtils.formatPattern(activity.getString(R.string.save_account_ok), username));
        } else {
            new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être sauvegardé", Constants.EXCEPTION_DATA_SAVE));
        }
    }

    private static boolean check(OgspyActivity activity, String username, String password, String serverUrl, String serverUnivers) throws OgspyException {
        boolean status = true;
        StringBuilder sb = new StringBuilder();

        if (username == null || username.length() == 0) {
            status = false;
            sb.append(activity.getString(R.string.save_account_username_ko));
        }
        if (password == null || password.length() == 0) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append(activity.getString(R.string.save_account_password_ko));
        }
        if (serverUrl == null || !serverUrl.matches("http://.*")) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append(activity.getString(R.string.save_account_server_url_ko));
        }
        // Check server univers
        if (serverUnivers == null || !serverUnivers.matches("http://.*")) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append(activity.getString(R.string.save_account_server_univers_ko));
        }
        if (sb.toString().length() > 0) {
            throw new OgspyException(sb.toString(), Constants.EXCEPTION_SAISIE);
        }
        return status;
    }
}
