package com.ogsteam.ogspy.network.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;

/**
 * Created by jp.tessier on 11/10/13.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
    public OgspyActivity activity;

    @Override
    protected void onPreExecute() {
        if (!canExecute()) {
            activity.showWaiting(false);
            Log.e(this.getClass().getSimpleName(), "DownloadTask non terminée, impossible d'en refaire une autre !");
        }
        activity.showWaiting(true);
    }

    @Override
    protected String doInBackground(String... strings) {
        return doInBackground(strings);
    }

    @Override
    protected void onCancelled() {
        activity.showWaiting(false);
        activity.showConnectivityProblem(true);
        Log.d("DownloadTask", "Dans onCancelled !");
    }

    @Override
    protected void onPostExecute(String result) {
        activity.showWaiting(false);
        activity.showConnectivityProblem(!activity.connection.isConnectingToInternet());
    }

    public boolean canExecute() {
        if (activity.downloadRentasTask == null) {
            return true;
        }
        boolean finished = activity.downloadRentasTask.getStatus().equals(Status.FINISHED); // over
        boolean running = activity.downloadRentasTask.getStatus().equals(Status.RUNNING); // in progress
        boolean pending = activity.downloadRentasTask.getStatus().equals(Status.PENDING); // not executed
        if (finished && !running && !pending) {
            return true;
        }
        return false;
    }
}
