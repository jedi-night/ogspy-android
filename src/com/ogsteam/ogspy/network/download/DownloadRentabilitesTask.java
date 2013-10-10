package com.ogsteam.ogspy.network.download;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.helpers.RentabilitesHelper;
import com.ogsteam.ogspy.utils.Constants;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.RentabilitesUtils;
import com.ogsteam.ogspy.utils.StringUtils;

import org.json.JSONObject;

public class DownloadRentabilitesTask extends AsyncTask<String, Integer, String> {
    public static final String DEBUG_TAG = DownloadRentabilitesTask.class.getSimpleName();
    private OgspyActivity activity;
    private String interval;
    private Activity waitActivity;
    private Intent intentWait;
    protected static JSONObject dataJsonFromAsyncTask;
    protected RentabilitesHelper helperRentabilites;

	public DownloadRentabilitesTask(OgspyActivity activity, String interval) {
        this.activity = activity;
        this.interval = interval;
        this.dataJsonFromAsyncTask = null;
        this.helperRentabilites = null;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
                //OgspyActivity.activity.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                //AnimationUtils.loadAnimation(activity, R.id.loadingPanel);
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), Constants.XTENSE_TYPE_RENTABILITES).concat("&interval="+interval);
				String data = HttpUtils.getUrl(url);
                if(data != null){
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    helperRentabilites = new RentabilitesHelper(dataJsonFromAsyncTask);
                }
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, activity.getString(R.string.download_problem),e);
		}
		return null;
	}

    protected void onProgressUpdate(Integer... progress) {

    }

	protected void onPostExecute(String result) {
        //OgspyActivity.activity.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        //waitActivity.stopService(intentWait);
        //AnimationUtils.loadAnimation(activity, R.id.loadingPanel);
        RentabilitesUtils.showRentabilites(helperRentabilites, activity);
	}
}
