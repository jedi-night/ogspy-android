package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.BuildConfig;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.responses.ServerOgspyResponse;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.ServerHelper;

import org.json.JSONObject;

public class DownloadServerTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadServerTask.class.getSimpleName();

    protected static JSONObject dataJsonFromAsyncTask;
    protected ServerHelper serverHelper;
    protected ServerOgspyResponse serverDatas;

    public DownloadServerTask() {
        dataJsonFromAsyncTask = null;
        this.serverHelper = null;
        typeDownload = DownloadType.SERVER;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                if (BuildConfig.DEBUG) {
                    DownloadInterface retrofit = getRetrofit();
                    serverDatas = retrofit.getServerData();
                } else {
                    UrlWithParameters request = prepareRequestForApi(Constants.XTENSE_TYPE_SERVER);
                    serverDatas = (ServerOgspyResponse) getFromServer(request, ServerOgspyResponse.class);
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getContext().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        //SpysUtils.showGeneral(serverHelper, null, null, activity);
        //super.onPostExecute(result);
    }
}
