package com.ogsteam.ogspy.network.download;

import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.responses.OgspyResponse;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jp.tessier on 11/10/13.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
    public DownloadType typeDownload;

    @Override
    protected void onPreExecute() {
        if (!OgspyActivity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
        /*if (!canExecute()) {
            activity.showWaiting(false);
            activity.showConnectivityProblem(false);
            Log.d(this.getClass().getSimpleName(), typeDownload.getLibelle() + " non terminé, en cours ou non executé; il est impossible d'en refaire un autre !");
        }*/
    }

    @Override
    protected String doInBackground(String... strings) {
        if (!OgspyActivity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
        return doInBackground(strings);
    }

    @Override
    protected void onCancelled() {
        Log.d(this.getClass().getSimpleName(), typeDownload.getLibelle() + " annulé !");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (!OgspyActivity.connection.isConnectingToInternet()) {
            this.cancel(true);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(DownloadTask.class.getSimpleName(), "Fin de l'affichage de chargement (" + this.typeDownload.getLibelle() + ")");
        this.cancel(true);
    }

    public static void executeDownload(DownloadTask downloadTask) {
        try {
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
                    OgspyActivity.downloadAllianceTask = new DownloadAllianceTask();
                    OgspyActivity.downloadAllianceTask.execute("do");
                } else if (DownloadType.SERVER.getType() == downloadType.getType()) {
                    OgspyActivity.downloadServerTask = new DownloadServerTask();
                    OgspyActivity.downloadServerTask.execute("do");
                } else if (DownloadType.SPY.getType() == downloadType.getType()) {
                    OgspyActivity.downloadSpysTask = new DownloadSpysTask();
                    OgspyActivity.downloadSpysTask.execute("do");
                } else if (DownloadType.HOSTILES.getType() == downloadType.getType()) {
                    OgspyActivity.downloadHostilesTask = new DownloadHostilesTask();
                    OgspyActivity.downloadHostilesTask.execute("do");
                } else if (DownloadType.RENTABILITES.getType() == downloadType.getType()) {
                    OgspyActivity.downloadRentasTask = new DownloadRentabilitesTask();
                    OgspyActivity.downloadRentasTask.execute("do");
                } else {
                    downloadTask.execute("do");
                }
                // Non traitée ET pas Finie ET pas En Cours
            } else if (pending && !finished && !running) {
                Log.d("DownloadTask", "Lancement de " + downloadType.getLibelle() + " car non traité");
                downloadTask.execute("do");
            } else {
                Log.d("DownloadTask", "Traitement de " + downloadType.getLibelle() + " ignoré car finished=" + finished + ", running=" + running + ", pending=" + pending);
            }
        } catch (Exception e) {
            Log.d("DownloadTask", "Problème lors de la récupération des données de " + downloadTask.typeDownload.getLibelle() + "; annulation en cours !");
            if (!downloadTask.isCancelled()) downloadTask.cancel(true);
        }
    }

    UrlWithParameters prepareRequestForToken() throws Exception{
        Account account = OgspyActivity.getSelectedAccount();

        UrlWithParameters urlWithParameters = new UrlWithParameters(StringUtils.formatPattern(Constants.URL_API_OGSPY, account.getServerUrl()));
        urlWithParameters.addParameter("action", "api");
        urlWithParameters.addParameter("login", account.getUsername());
        urlWithParameters.addParameter("password", OgspyUtils.enryptPassword(account.getPassword()));

        return urlWithParameters;
    }

    UrlWithParameters prepareRequestForApi(String data) throws Exception{
        Account account = OgspyActivity.getSelectedAccount();

        UrlWithParameters urlWithParameters = new UrlWithParameters(StringUtils.formatPattern(Constants.URL_API_OGSPY, account.getServerUrl()));
        urlWithParameters.addParameter("action", "api");
        urlWithParameters.addParameter("token", OgspyActivity.apiToken.getApi_token());
        urlWithParameters.addParameter("data", data);

        return urlWithParameters;
    }

    OgspyResponse getFromServer(UrlWithParameters request, Class<? extends OgspyResponse> ogspyResponse){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(request.getUrl(), ogspyResponse, request.getParameters());
    }
}
