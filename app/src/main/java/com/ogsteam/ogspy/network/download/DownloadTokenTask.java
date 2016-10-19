package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.responses.ApiToken;

public class DownloadTokenTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadTokenTask.class.getSimpleName();

    private ApiToken apiToken;

    public DownloadTokenTask() {
        typeDownload = DownloadType.ALLIANCE;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                UrlWithParameters request = prepareRequestForToken();
                apiToken = (ApiToken) getFromServer(request, ApiToken.class);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getApplication().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);

            FirebaseCrash.logcat(Log.ERROR, DEBUG_TAG, e.getMessage());
            FirebaseCrash.report(e);
            if (!isCancelled()) {
                this.cancel(true);
            }
        }
        return null;
    }
}
