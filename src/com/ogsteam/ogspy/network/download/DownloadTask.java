package com.ogsteam.ogspy.network.download;

import android.os.AsyncTask;

import com.ogsteam.ogspy.OgspyActivity;

/**
 * Created by jp.tessier on 11/10/13.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
    public OgspyActivity activity;

    @Override
    protected void onPreExecute() {
        activity.showWaiting(true);
    }

    @Override
    protected String doInBackground(String... strings) {
        return doInBackground(strings);
    }

    @Override
    protected void onCancelled() {
        activity.showWaiting(false);
    }

    @Override
    protected void onPostExecute(String result) {
        activity.showWaiting(false);
    }
}
