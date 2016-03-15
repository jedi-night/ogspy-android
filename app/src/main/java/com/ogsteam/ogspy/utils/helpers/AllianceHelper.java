package com.ogsteam.ogspy.utils.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Jedinight on 04/06/13.
 */
public class AllianceHelper {
    private String ownAlliance="";
    private String nbMembers="";

    public AllianceHelper(JSONObject datas){
        try{
            if(datas.has("alliance") && datas.getJSONArray("alliance") != null){
                JSONArray alliance = datas.getJSONArray("alliance");
                if(alliance!=null && alliance.length() > 0){
                     ownAlliance = alliance.getJSONArray(0).getString(0);
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluation des donnees Alliance");
        }
    }

    public String getOwnAlliance() {
        return ownAlliance;
    }

    public String getNbMembers() {
        return nbMembers;
    }
}
