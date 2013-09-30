package com.ogsteam.ogspy.network.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.helpers.SpysHelper;
import com.ogsteam.ogspy.utils.Constants;
import com.ogsteam.ogspy.utils.GeneralUtils;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;

import org.json.JSONObject;

public class DownloadSpysTask extends AsyncTask<String, Integer, String> {
    public static final String DEBUG_TAG = DownloadSpysTask.class.getSimpleName();
    private OgspyActivity activity;
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
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), Constants.XTENSE_TYPE_SPYS);
				String data = HttpUtils.getUrl(url);
                if(data != null){
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    spysHelper = new SpysHelper(dataJsonFromAsyncTask);
                }
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, activity.getString(R.string.download_problem),e);
		}
		return null;
	}

    protected void onProgressUpdate(Integer... progress) {
     //setProgressPercent(progress[0]);
    }

	protected void onPostExecute(String result) {
        GeneralUtils.showGeneral(null, null, spysHelper, activity);
	}

    public SpysHelper getSpysHelper() {
        return spysHelper;
    }
}