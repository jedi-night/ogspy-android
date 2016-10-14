package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.AllianceHelper;
import com.ogsteam.ogspy.utils.helpers.Constants;

import org.json.JSONObject;

public class DownloadAllianceTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadAllianceTask.class.getSimpleName();

    protected static JSONObject dataJsonFromAsyncTask;
    protected AllianceHelper allianceHelper;

    public DownloadAllianceTask() {
        dataJsonFromAsyncTask = null;
        this.allianceHelper = null;
        typeDownload = DownloadType.ALLIANCE;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION,
                        account.getServerUrl(), account.getUsername(),
                        OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(),
                        OgspyActivity.versionAndroid, OgspyActivity.getVersionOgspy(), OgspyActivity.getDeviceName(), Constants.XTENSE_TYPE_ALLIANCE);
                String data = HttpUtils.getUrl(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    allianceHelper = new AllianceHelper(dataJsonFromAsyncTask);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getApplication().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        //SpysUtils.showGeneral(null, allianceHelper, null, activity);
        //super.onPostExecute(result);
    }

    public AllianceHelper getAllianceHelper() {
        return allianceHelper;
    }
}
