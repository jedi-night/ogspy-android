package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.ui.displays.GeneralUtils;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.SpysHelper;

import org.json.JSONObject;

public class DownloadSpysTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadSpysTask.class.getSimpleName();

    protected static JSONObject dataJsonFromAsyncTask;
    protected SpysHelper spysHelper;

    public DownloadSpysTask(OgspyActivity activity) {
        this.activity = activity;
        this.dataJsonFromAsyncTask = null;
        this.spysHelper = null;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (!activity.getHandlerAccount().getAllAccounts().isEmpty()) {
                Account account = activity.getHandlerAccount().getAccountById(0);
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), activity.getDeviceName(), Constants.XTENSE_TYPE_SPYS);
                String data = HttpUtils.getUrl(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    spysHelper = new SpysHelper(dataJsonFromAsyncTask);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, activity.getString(R.string.download_problem), e);
            if (!isCancelled()) cancel(true);
            onCancelled();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        GeneralUtils.showGeneral(null, null, spysHelper, activity);
        super.onPostExecute(result);
    }

    public SpysHelper getSpysHelper() {
        return spysHelper;
    }
}
