package com.ogsteam.ogspy.network.download;

import android.util.Log;
import android.widget.Spinner;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.fragments.tabs.SpysFragment;
import com.ogsteam.ogspy.ui.displays.SpysUtils;
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
    private String interval;

    public DownloadSpysTask(String interval) {
        dataJsonFromAsyncTask = null;
        this.spysHelper = null;
        this.interval = interval;
        typeDownload = DownloadType.SPY;
    }

    public DownloadSpysTask() {
        dataJsonFromAsyncTask = null;
        this.spysHelper = null;
        Spinner rentaInterval = SpysFragment.getInterval();
        int positionSelected = rentaInterval == null ? 3 : rentaInterval.getSelectedItemPosition();
        this.interval = OgspyApplication.getContext().getResources().getStringArray(R.array.interval_values)[positionSelected];
        typeDownload = DownloadType.SPY;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), OgspyActivity.versionAndroid, OgspyActivity.getVersionOgspy(), OgspyActivity.getDeviceName(), Constants.XTENSE_TYPE_SPYS).concat("&interval=" + interval);
                String data = HttpUtils.getUrl(url);
                if (data != null) {
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    spysHelper = new SpysHelper(dataJsonFromAsyncTask);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getApplication().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        SpysUtils.showSpys(null, spysHelper);
        super.onPostExecute(result);
    }

    public SpysHelper getSpysHelper() {
        return spysHelper;
    }
}
