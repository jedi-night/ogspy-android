package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.ServerHelper;

import org.json.JSONObject;

public class DownloadServerTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadServerTask.class.getSimpleName();

    protected static JSONObject dataJsonFromAsyncTask;
    protected ServerHelper serverHelper;

    public DownloadServerTask() {
        dataJsonFromAsyncTask = null;
        this.serverHelper = null;
        typeDownload = DownloadType.SERVER;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), OgspyActivity.versionAndroid, OgspyActivity.getVersionOgspy(), OgspyActivity.getDeviceName(), Constants.XTENSE_TYPE_SERVER) + "&gcmRegid=" + OgspyActivity.getRegId();
                String data = HttpUtils.getUrl(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    serverHelper = new ServerHelper(dataJsonFromAsyncTask);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getContext().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        //SpysUtils.showGeneral(serverHelper, null, null, activity);
        //super.onPostExecute(result);
    }
}
