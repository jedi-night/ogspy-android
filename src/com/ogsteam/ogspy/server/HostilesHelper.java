package com.ogsteam.ogspy.server;

import android.util.Log;

import org.json.JSONObject;

import java.util.TreeMap;

/**
 * Created by jp.tessier on 04/06/13.
 */
public class HostilesHelper {
    private boolean isAttack = false;
    private TreeMap<String,Cible> cibles;

    public HostilesHelper(JSONObject datas){
        cibles = new TreeMap<String, Cible>();
        try{
            isAttack = datas.getBoolean("isAttack");

            JSONObject hostile = datas.getJSONObject("hostile");
/*            if(hostile.length() > 0 && isAttack){
                JSONArray usersCible = hostile.getJSONArray("user");

                for(int i=0;i < usersCible.length(); i++){
                    String cible = usersCible.getString(i);

                }
            }
            */
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluration des donnees Hostiles");
        }
    }

    private class Cible {
        public String player;
        public String attacker;
        public String from;

        public Cible(JSONObject cible){

        }
    }
}
