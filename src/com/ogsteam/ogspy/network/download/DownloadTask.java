package com.ogsteam.ogspy.network.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;

/**
 * Created by jp.tessier on 11/10/13.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
    public OgspyActivity activity;
    public DownloadType typeDownload;

    @Override
    protected void onPreExecute() {
        if (!activity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
        if (!canExecute()) {
            activity.showWaiting(false);
            activity.showConnectivityProblem(false);
            Log.d(this.getClass().getSimpleName(), typeDownload.getLibelle() + " non terminé, en cours ou non executé; il est impossible d'en refaire un autre !");
        }
        activity.showWaiting(true);
    }

    @Override
    protected String doInBackground(String... strings) {
        if (!activity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
        return doInBackground(strings);
    }

    @Override
    protected void onCancelled() {
        activity.showWaiting(false);
        activity.showConnectivityProblem(true);
        Log.d(this.getClass().getSimpleName(), typeDownload.getLibelle() + " annulé !");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (!activity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        activity.showWaiting(false);
        activity.showConnectivityProblem(!activity.connection.isConnectingToInternet());
    }

    public boolean canExecute() {
        int thisTypeDownload = this.typeDownload.getType();
        DownloadTask downloadTask = null;
        if (DownloadType.ALLIANCE.getType() == thisTypeDownload) {
            downloadTask = activity.downloadAllianceTask;
        } else if (DownloadType.SERVER.getType() == thisTypeDownload) {
            downloadTask = activity.downloadServerTask;
        } else if (DownloadType.SPY.getType() == thisTypeDownload) {
            downloadTask = activity.downloadSpysTask;
        } else if (DownloadType.HOSTILES.getType() == thisTypeDownload) {
            downloadTask = activity.downloadHostilesTask;
        } else if (DownloadType.RENTABILITES.getType() == thisTypeDownload) {
            downloadTask = activity.downloadRentasTask;
        }

        if (downloadTask == null) {
            return true;
        }
        boolean finished = downloadTask.getStatus().equals(Status.FINISHED); // over
        boolean running = downloadTask.getStatus().equals(Status.RUNNING); // in progress
        boolean pending = downloadTask.getStatus().equals(Status.PENDING); // not executed
        if (finished && !running && !pending) {
            return true;
        }
        return false;
    }
}
