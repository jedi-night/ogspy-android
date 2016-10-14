package com.ogsteam.ogspy.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private Context _context;
    private StringBuilder infos;

    public ConnectionDetector(Context context) {
        this._context = context;
        this.infos = new StringBuilder();
    }

    /**
     * Checking for all possible internet providers
     * *
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    this.infos.append(anInfo.getDetailedState());
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED || anInfo.getState() == NetworkInfo.State.CONNECTING) {
                        return true;
                    }
                    this.infos.append(" ");
                }
            }
        }
        return false;
    }

    public String getInfos() {
        return this.infos.toString();
    }
}
