package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
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

	public DownloadHostilesTask(OgspyActivity activity) {
        this.activity = activity;
        this.dataJsonFromAsyncTask = null;
        this.helperHostile = null;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), Constants.XTENSE_TYPE_HOSTILES);
				String data = HttpUtils.getUrl(url);
                if(data != null){
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    helperHostile = new HostilesHelper(dataJsonFromAsyncTask);
                }
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, activity.getString(R.string.download_problem),e);
            activity.showConnectivityProblem(true);
        }
		return null;
	}

	protected void onPostExecute(String result) {
        HostileUtils.showHostiles(helperHostile,activity);
        super.onPostExecute(result);
	}
}
