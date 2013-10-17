package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.ui.displays.GeneralUtils;
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

	public DownloadAllianceTask(OgspyActivity activity) {
        this.activity = activity;
        this.dataJsonFromAsyncTask = null;
        this.allianceHelper = null;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers(), activity.versionAndroid, activity.getVersionOgspy(), Constants.XTENSE_TYPE_ALLIANCE);
				String data = HttpUtils.getUrl(url);
                if(data != null){
                    dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                    allianceHelper = new AllianceHelper(dataJsonFromAsyncTask);
                }
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, activity.getString(R.string.download_problem),e);
		}
		return null;
	}

	protected void onPostExecute(String result) {
        GeneralUtils.showGeneral(null, allianceHelper, null, activity);
	}

    public AllianceHelper getAllianceHelper() {
        return allianceHelper;
    }
}
