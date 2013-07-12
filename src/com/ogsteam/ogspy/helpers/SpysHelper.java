package com.ogsteam.ogspy.helpers;

import android.util.Log;

import com.ogsteam.ogspy.utils.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jedinight on 04/06/13.
 */
public class SpysHelper {
    private ArrayList<Pair> mostCuriousAlliances;
    private ArrayList<Pair> mostCuriousPlayers;

    public SpysHelper(JSONObject datas){
        try{
            mostCuriousPlayers = new ArrayList<Pair>();
            mostCuriousAlliances = new ArrayList<Pair>();
            if(datas.has("mostCuriousPlayer") && datas.getJSONArray("mostCuriousPlayer") != null){
                JSONArray spysPlayers = datas.getJSONArray("mostCuriousPlayer");
                if(spysPlayers!=null){
                    for (int i =0; i < spysPlayers.length(); i++){
                        JSONArray player = spysPlayers.getJSONArray(i);
                        StringBuilder playerLibelle = new StringBuilder(player.getString(0));
                        if(player.getString(1).length() > 0){
                            playerLibelle.append(" [").append(player.getString(1)).append("]");
                        }
                        mostCuriousPlayers.add(new Pair(playerLibelle.toString(),player.getString(2)));
                    }
                }
            }
            if(datas.has("mostCuriousAlliance") && datas.getJSONArray("mostCuriousAlliance") != null){
                JSONArray spysAlliances = datas.getJSONArray("mostCuriousAlliance");
                if(spysAlliances!=null){
                    for (int i =0; i < spysAlliances.length(); i++){
                        JSONArray alliance = spysAlliances.getJSONArray(i);
                        StringBuilder allianceLibelle = new StringBuilder();
                        if(alliance.getString(0).length() > 0){
                            allianceLibelle.append(alliance.getString(0));
                        } else {
                            allianceLibelle.append(" -NC- ");
                        }
                        mostCuriousAlliances.add(new Pair(allianceLibelle.toString(),alliance.getString(1)));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluation des donnees General");
        }
    }

    public ArrayList<Pair> getMostCuriousAlliances() {
        return mostCuriousAlliances;
    }

    public ArrayList<Pair> getMostCuriousPlayers() {
        return mostCuriousPlayers;
    }
}
