package com.ogsteam.ogspy.preferences;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;

public class Accounts {

    public static void saveAccount(String username, String password, String serverUrl, String serverUnivers) {
        Account lastAccount = OgspyActivity.getHandlerAccount().getLastAccount();
        if (lastAccount != null) {
            if (OgspyActivity.getHandlerAccount().addAccount(new Account((lastAccount.getId() + 1), username, password, serverUrl, serverUnivers)) != -1) {
                CommonUtilities.displayMessage(OgspyApplication.getContext(), StringUtils.formatPattern(OgspyApplication.getContext().getString(R.string.save_account_ok), username + " - " + OgspyUtils.getUniversNameFromUrl(serverUnivers)));
            } else {
                new DialogHandler().showException(OgspyApplication.getContext(), new OgspyException("Le compte n'a pu être créé", Constants.EXCEPTION_DATA_SAVE));
            }
        } else {
            if (OgspyActivity.getHandlerAccount().addAccount(new Account(0, username, password, serverUrl, serverUnivers)) != -1) {
                CommonUtilities.displayMessage(OgspyApplication.getContext(), StringUtils.formatPattern(OgspyApplication.getContext().getString(R.string.save_account_ok), username + " - " + OgspyUtils.getUniversNameFromUrl(serverUnivers)));
            } else {
                new DialogHandler().showException(OgspyApplication.getContext(), new OgspyException("Le compte n'a pu être créé", Constants.EXCEPTION_DATA_SAVE));
            }
        }

    }

    public static void updateAccount(String id, String username, String password, String serverUrl, String serverUnivers) {
        if (OgspyActivity.getHandlerAccount().updateAccount(new Account(Integer.parseInt(id), username, password, serverUrl, serverUnivers)) != -1) {
            CommonUtilities.displayMessage(OgspyApplication.getContext(), StringUtils.formatPattern(OgspyApplication.getContext().getString(R.string.save_account_ok), username + " - " + OgspyUtils.getUniversNameFromUrl(serverUnivers)));
        } else {
            new DialogHandler().showException(OgspyApplication.getContext(), new OgspyException("Le compte n'a pu être sauvegardé", Constants.EXCEPTION_DATA_SAVE));
        }
    }

}
