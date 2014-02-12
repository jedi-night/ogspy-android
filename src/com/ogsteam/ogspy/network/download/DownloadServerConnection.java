package com.ogsteam.ogspy.network.download;

import android.preference.ListPreference;
import android.util.Log;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyPreferencesActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.preferences.Accounts;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.ServerHelper;

import org.json.JSONObject;

public class DownloadServerConnection extends DownloadTask {
    public static final String DEBUG_TAG = DownloadServerConnection.class.getSimpleName();

    protected static JSONObject dataJsonFromAsyncTask;
    protected ServerHelper serverHelper;
    protected Account account = null;
    protected String accountCreation;

    public DownloadServerConnection(OgspyActivity activity, Account account, String accoutnCreation) {
        this.activity = activity;
        this.dataJsonFromAsyncTask = null;
        this.serverHelper = null;
        typeDownload = DownloadType.SERVER_CONNECTION;
        this.account = account;
        this.accountCreation = accoutnCreation;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (account != null) {
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), activity.getDeviceName(), Constants.XTENSE_TYPE_SERVER) + "&gcmRegid=" + activity.getRegId();
                String data = HttpUtils.getUrlWithoutDisplayConnectivityProblem(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    serverHelper = new ServerHelper(dataJsonFromAsyncTask);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, activity.getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        //SpysUtils.showGeneral(serverHelper, null, null, activity);
        //super.onPostExecute(result);
        if (serverHelper == null) {
            CommonUtilities.displayMessage(DialogActivity.activity, "La connexion n'a pu être établie avec ce compte, veuillez vérifier les informations saisies !");
        } else {
            if (DialogActivity.ACCOUNT_NEW.equals(accountCreation)) {
                Accounts.saveAccount(OgspyActivity.activity, account.getUsername(), account.getPassword(), account.getServerUrl(), account.getServerUnivers());
            } else {
                Accounts.updateAccount(OgspyActivity.activity, String.valueOf(account.getId()), account.getUsername(), account.getPassword(), account.getServerUrl(), account.getServerUnivers());
            }
            OgspyPreferencesActivity.activity.refreshAcountsList((ListPreference) OgspyPreferencesActivity.activity.findPreference("prefs_accounts"));
            DialogActivity.activity.finish();
        }
    }
}
