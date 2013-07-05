package com.ogsteam.ogspy.helpers;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Jedinight on 04/06/13.
 */
public class ServerHelper {
    private String serverName="";

    public ServerHelper(JSONObject datas){
        try{
            if(datas.has("server")){
                serverName = datas.getString("server");
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluation des donnees Server");
        }
    }

    public String getServerName() {
        return serverName;
    }
}
