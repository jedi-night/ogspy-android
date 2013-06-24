package com.ogsteam.ogspy.network;

import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.server.HostilesHelper;
import com.ogsteam.ogspy.utils.Constants;
import com.ogsteam.ogspy.utils.HostileUtils;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;

import org.json.JSONObject;

public class DownloadTask extends AsyncTask<String, Integer, String> {
    private OgspyActivity activity;
	protected static String hostilesData;
	protected static JSONObject dataJsonFromAsyncTask;
    protected HostilesHelper helperHostile;

	public DownloadTask(OgspyActivity activity) {
        this.activity = activity;
        this.hostilesData = null;
        this.dataJsonFromAsyncTask = null;
        this.helperHostile = null;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy());
				String data = HttpUtils.getUrl(url);
				dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                helperHostile = new HostilesHelper(dataJsonFromAsyncTask);
				hostilesData = OgspyUtils.traiterReponseHostiles(helperHostile, activity);
			}
		} catch (Exception e) {
			Log.e(OgspyActivity.DEBUG_TAG, activity.getString(R.string.download_problem),e);
		}
		return null;
	}

    protected void onProgressUpdate(Integer... progress) {
     //setProgressPercent(progress[0]);
    }

	protected void onPostExecute(String result) {
        HostileUtils.showHostiles(helperHostile,activity);
	}
}
