package com.ogsteam.ogspy.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.utils.Constants;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;


public class DownloadTask extends AsyncTask<String, Integer, String> {
    private OgspyActivity activity;
	protected static String dataFromAsyncTask; 
    
	public DownloadTask(OgspyActivity activity) {
        this.activity = activity;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers());
				dataFromAsyncTask = OgspyUtils.traiterReponseHostiles(HttpUtils.downloadUrl(url), activity);
			}
		} catch (Exception e) {
			Log.e(OgspyActivity.DEBUG_TAG, "Problème lors du telechargement !",e);
		}
		return null;
	}

     protected void onProgressUpdate(Integer... progress) {
         //setProgressPercent(progress[0]);
     }

	protected void onPostExecute(String result) {
		if(activity.findViewById(R.id.response_ogspy) != null){
			((EditText) activity.findViewById(R.id.response_ogspy)).setText(dataFromAsyncTask);
		}
	}

    public static String getDataFromAsyncTask() {
		return dataFromAsyncTask;
	}

	public static void setDataFromAsyncTask(String dataFromAsyncTask) {
		DownloadTask.dataFromAsyncTask = dataFromAsyncTask;
	}

}
