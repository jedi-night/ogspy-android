package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.ui.displays.HostileUtils;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.HostilesHelper;

import org.json.JSONObject;

public class DownloadHostilesTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadHostilesTask.class.getSimpleName();

    protected static JSONObject dataJsonFromAsyncTask;
    protected HostilesHelper helperHostile;

    public DownloadHostilesTask() {
        dataJsonFromAsyncTask = null;
        this.helperHostile = null;
        typeDownload = DownloadType.HOSTILES;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), OgspyActivity.versionAndroid, OgspyActivity.getVersionOgspy(), OgspyActivity.getDeviceName(), Constants.XTENSE_TYPE_HOSTILES);
                String data = HttpUtils.getUrl(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    helperHostile = new HostilesHelper(dataJsonFromAsyncTask);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getApplication().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    public HostilesHelper getHelperHostile() {
        return helperHostile;
    }

    protected void onPostExecute(String result) {
        HostileUtils.showHostiles(helperHostile);
        super.onPostExecute(result);
    }
}
