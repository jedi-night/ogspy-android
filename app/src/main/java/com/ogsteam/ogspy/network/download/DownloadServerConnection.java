package com.ogsteam.ogspy.network.download;

import android.preference.ListPreference;

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

    protected DialogActivity dialogActivity;
    protected static JSONObject dataJsonFromAsyncTask;
    protected ServerHelper serverHelper;
    protected Account account = null;
    protected String accountCreation;

    public DownloadServerConnection(OgspyActivity activity, DialogActivity dialogActivity, Account account, String accoutnCreation) {
        this.activity = activity;
        this.dialogActivity = dialogActivity;
        this.dataJsonFromAsyncTask = null;
        this.serverHelper = null;
        typeDownload = DownloadType.SERVER_CONNECTION;
        this.account = account;
        this.accountCreation = accoutnCreation;
    }

    @Override
    protected void onPreExecute() {
        CommonUtilities.displayMessageDebug(activity, this.getClass(), "Connecté à internet ? => " + activity.connection.isConnectingToInternet() + "\nInfos : " + activity.connection.getInfos());
        if (!activity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
        /*if (!canExecute()) {
            activity.showWaiting(false);
            activity.showConnectivityProblem(false);
            Log.d(this.getClass().getSimpleName(), typeDownload.getLibelle() + " non terminé, en cours ou non executé; il est impossible d'en refaire un autre !");
        }*/
        if (dialogActivity != null) dialogActivity.showWaiting(true);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (account != null) {
                CommonUtilities.displayMessageDebug(activity, this.getClass(), "Récupération des informations de connection du compte (" + account.getUsername() + ")");
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), activity.getDeviceName(), Constants.XTENSE_TYPE_SERVER) + "&gcmRegid=" + activity.getRegId();
                String data = HttpUtils.getUrlWithoutDisplayConnectivityProblem(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    serverHelper = new ServerHelper(dataJsonFromAsyncTask);
                    CommonUtilities.displayMessageDebug(activity, this.getClass(), "Serveur Ogspy : " + serverHelper.getServerName());
                } else {
                    CommonUtilities.displayMessageDebug(activity, this.getClass(), "Aucune donnée récupérée concernant le compte (" + account.getUsername() + ")");
                }
            }
        } catch (Exception e) {
            CommonUtilities.displayMessageDebugAndLog(activity, this.getClass(), activity.getString(R.string.download_problem), e, " (" + typeDownload.getLibelle() + ")");
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        //SpysUtils.showGeneral(serverHelper, null, null, activity);
        //super.onPostExecute(result);
        if (dialogActivity != null) dialogActivity.showWaiting(false);
        if (serverHelper == null || serverHelper.getServerName().isEmpty()) {
            CommonUtilities.displayMessage(DialogActivity.activity, "La connexion n'a pu être établie avec ce compte, veuillez vérifier les informations saisies !");
        } else {
            if (DialogActivity.ACCOUNT_NEW.equals(accountCreation)) {
                CommonUtilities.displayMessageDebug(activity, this.getClass(), "Création du compte en cours ...");
                Accounts.saveAccount(OgspyActivity.activity, account.getUsername(), account.getPassword(), account.getServerUrl(), account.getServerUnivers());
            } else {
                CommonUtilities.displayMessageDebug(activity, this.getClass(), "Mise à jour du compte en cours ...");
                Accounts.updateAccount(OgspyActivity.activity, String.valueOf(account.getId()), account.getUsername(), account.getPassword(), account.getServerUrl(), account.getServerUnivers());
            }
            CommonUtilities.displayMessageDebug(activity, this.getClass(), "Refresh des comptes en cours");
            OgspyPreferencesActivity.activity.refreshAcountsList((ListPreference) OgspyPreferencesActivity.activity.findPreference("prefs_accounts"));
            CommonUtilities.displayMessageDebug(activity, this.getClass(), "Refresh des comptes terminé");
            DialogActivity.activity.finish();
        }
    }
}
