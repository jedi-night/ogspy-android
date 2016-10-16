package com.ogsteam.ogspy.network.download;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.responses.AllianceResponse;
import com.ogsteam.ogspy.utils.helpers.Constants;

public class DownloadAllianceTask extends DownloadTask {
    public static final String DEBUG_TAG = DownloadAllianceTask.class.getSimpleName();

    private AllianceResponse allianceDatas;

    public DownloadAllianceTask() {
        typeDownload = DownloadType.ALLIANCE;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Account account = OgspyActivity.getSelectedAccount();
            if (!OgspyActivity.getHandlerAccount().getAllAccounts().isEmpty() && account != null) {
                UrlWithParameters request = prepareRequestForApi(Constants.XTENSE_TYPE_ALLIANCE);
                allianceDatas = (AllianceResponse) getFromServer(request, AllianceResponse.class);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, OgspyApplication.getApplication().getString(R.string.download_problem) + " (" + typeDownload.getLibelle() + ")", e);
            if (!isCancelled()) this.cancel(true);
        }
        return null;
    }
}
