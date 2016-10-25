package com.ogsteam.ogspy.network.download;

import android.preference.ListPreference;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.ogsteam.ogspy.BuildConfig;
import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.OgspyPreferencesActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.responses.ServerOgspyResponse;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.preferences.Accounts;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.ServerHelper;

import org.json.JSONObject;

public class DownloadServerConnection extends DownloadTask {

    protected static JSONObject dataJsonFromAsyncTask;
    protected ServerHelper serverHelper;
    protected ServerOgspyResponse serverDatas;
    protected Account account = null;
    protected String accountCreation;

    public DownloadServerConnection(Account account, String accoutnCreation) {
        dataJsonFromAsyncTask = null;
        this.serverHelper = null;
        typeDownload = DownloadType.SERVER_CONNECTION;
        this.account = account;
        this.accountCreation = accoutnCreation;
    }

    @Override
    protected void onPreExecute() {
        CommonUtilities.displayMessageDebug(OgspyApplication.getContext(), this.getClass(), "Connecté à internet ? => " + OgspyActivity.connection.isConnectingToInternet() + "\nInfos : " + OgspyActivity.connection.getInfos());
        if (!OgspyActivity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (account != null) {
                if (BuildConfig.DEBUG) {
                    DownloadInterface retrofit = getRetrofit(account);
                    serverDatas = retrofit.getServerData();
                } else {
                    UrlWithParameters request = prepareRequestForApi(Constants.XTENSE_TYPE_SERVER);
                    serverDatas = (ServerOgspyResponse) getFromServer(request, ServerOgspyResponse.class);
                }
            }
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, OgspyUtils.getTagClass(this.getClass()), "Download server failded !".concat(" ").concat(e.getMessage()));
            CommonUtilities.displayMessageDebugAndLog(OgspyApplication.getContext(), this.getClass(), OgspyApplication.getApplication().getString(R.string.download_problem), e, " (" + typeDownload.getLibelle() + ")");
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        //SpysUtils.showGeneral(serverHelper, null, null, activity);
        //super.onPostExecute(result);
        if (serverHelper == null || serverHelper.getServerName().isEmpty()) {
            CommonUtilities.displayMessage(DialogActivity.activity, "La connexion n'a pu être établie avec ce compte, veuillez vérifier les informations saisies !");
        } else {
            if (DialogActivity.ACCOUNT_NEW.equals(accountCreation)) {
                CommonUtilities.displayMessageDebug(OgspyApplication.getContext(), this.getClass(), "Création du compte en cours ...");
                Accounts.saveAccount(account.getUsername(), account.getPassword(), account.getServerUrl(), account.getServerUnivers());
            } else {
                CommonUtilities.displayMessageDebug(OgspyApplication.getContext(), this.getClass(), "Mise à jour du compte en cours ...");
                Accounts.updateAccount(String.valueOf(account.getId()), account.getUsername(), account.getPassword(), account.getServerUrl(), account.getServerUnivers());
            }
            CommonUtilities.displayMessageDebug(OgspyApplication.getContext(), this.getClass(), "Refresh des comptes en cours");
            OgspyPreferencesActivity.activity.refreshAcountsList((ListPreference) OgspyPreferencesActivity.activity.findPreference("prefs_accounts"));
            CommonUtilities.displayMessageDebug(OgspyApplication.getContext(), this.getClass(), "Refresh des comptes terminé");
            DialogActivity.activity.finish();
        }
    }
}
