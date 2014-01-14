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
        /*if (!canExecute()) {
            activity.showWaiting(false);
            activity.showConnectivityProblem(false);
            Log.d(this.getClass().getSimpleName(), typeDownload.getLibelle() + " non terminé, en cours ou non executé; il est impossible d'en refaire un autre !");
        }*/
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
        this.cancel(true);
    }

    public static void executeDownload(OgspyActivity activity, DownloadTask downloadTask) {
        boolean finished = true;
        boolean running = false;
        boolean pending = false;

        DownloadType downloadType = downloadTask == null ? null : downloadTask.typeDownload;
        // Le download a déjà été lancé
        if (downloadType != null) {
            finished = downloadTask.getStatus().equals(Status.FINISHED); // over
            running = downloadTask.getStatus().equals(Status.RUNNING); // in progress
            pending = downloadTask.getStatus().equals(Status.PENDING); // not executed
        }

        // Finie ET pas En Cours ET pas En Attente
        if (finished && !running && !pending) {
            Log.d("DownloadTask", "Lancement de " + downloadType.getLibelle() + " car fini");
            if (DownloadType.ALLIANCE.getType() == downloadType.getType()) {
                activity.downloadAllianceTask = new DownloadAllianceTask(activity);
                activity.downloadAllianceTask.execute(new String[]{"do"});
            } else if (DownloadType.SERVER.getType() == downloadType.getType()) {
                activity.downloadServerTask = new DownloadServerTask(activity);
                activity.downloadServerTask.execute(new String[]{"do"});
            } else if (DownloadType.SPY.getType() == downloadType.getType()) {
                activity.downloadSpysTask = new DownloadSpysTask(activity);
                activity.downloadSpysTask.execute(new String[]{"do"});
            } else if (DownloadType.HOSTILES.getType() == downloadType.getType()) {
                activity.downloadHostilesTask = new DownloadHostilesTask(activity);
                activity.downloadHostilesTask.execute(new String[]{"do"});
            } else if (DownloadType.RENTABILITES.getType() == downloadType.getType()) {
                activity.downloadRentasTask = new DownloadRentabilitesTask(activity);
                activity.downloadRentasTask.execute(new String[]{"do"});
            }
            // Non traitée ET pas Finie ET pas En Cours
        } else if (pending && !finished && !running) {
            Log.d("DownloadTask", "Lancement de " + downloadType.getLibelle() + " car non traité");
            downloadTask.execute(new String[]{"do"});
        } else {
            Log.d("DownloadTask", "Traitement de " + downloadType.getLibelle() + " ignoré car finished=" + finished + ", running=" + running + ", pending=" + pending);
        }
    }
}
