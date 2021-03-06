package com.ogsteam.ogspy.utils.helpers;

import android.util.Log;

import com.ogsteam.ogspy.utils.objects.HighScore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Jedinight on 04/06/13.
 */
public class SpysHelper {
    private ArrayList<Pair> mostCuriousAlliances;
    private ArrayList<Triplet> mostCuriousPlayers;
    private TreeMap<String, HighScore> highscores;
    private TreeMap<String, HighScore> highscoresAlliance;

    public SpysHelper(JSONObject datas) {
        try {
            mostCuriousPlayers = new ArrayList<Triplet>();
            mostCuriousAlliances = new ArrayList<Pair>();
            highscores = new TreeMap<String, HighScore>();
            highscoresAlliance = new TreeMap<String, HighScore>();

            if (datas.has("mostCuriousPlayer") && datas.getJSONArray("mostCuriousPlayer") != null) {
                JSONArray spysPlayers = datas.getJSONArray("mostCuriousPlayer");
                if (spysPlayers != null) {
                    for (int i = 0; i < spysPlayers.length(); i++) {
                        JSONArray player = spysPlayers.getJSONArray(i);
                        StringBuilder playerLibelle = new StringBuilder(player.getString(0));
                        if (player.getString(1).length() > 0) {
                            playerLibelle.append(" [").append(player.getString(1)).append("]");
                        }
                        mostCuriousPlayers.add(new Triplet(player.getString(0), playerLibelle.toString(), player.getString(2)));
                        highscores.put(player.getString(0), new HighScore(getValueJSON(player, 3), getValueJSON(player, 4), getValueJSON(player, 5), getValueJSON(player, 6), getIntValueJSON(player, 7), getIntValueJSON(player, 8), getIntValueJSON(player, 9), getIntValueJSON(player, 10)));
                    }
                }
            }
            if (datas.has("mostCuriousAlliance") && datas.getJSONArray("mostCuriousAlliance") != null) {
                JSONArray spysAlliances = datas.getJSONArray("mostCuriousAlliance");
                if (spysAlliances != null) {
                    for (int i = 0; i < spysAlliances.length(); i++) {
                        JSONArray alliance = spysAlliances.getJSONArray(i);
                        StringBuilder allianceLibelle = new StringBuilder();
                        if (alliance.getString(0).length() > 0) {
                            allianceLibelle.append(alliance.getString(0));
                        } else {
                            allianceLibelle.append(" -NC- ");
                        }
                        mostCuriousAlliances.add(new Pair(allianceLibelle.toString(), alliance.getString(1)));
                        highscoresAlliance.put(alliance.getString(0), new HighScore(getValueJSON(alliance, 2), getValueJSON(alliance, 3), getValueJSON(alliance, 4), getValueJSON(alliance, 5), getIntValueJSON(alliance, 6), getIntValueJSON(alliance, 7), getIntValueJSON(alliance, 8), getIntValueJSON(alliance, 9)));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Probleme lors de l'evaluation des donnees General");
        }
    }

    public ArrayList<Pair> getMostCuriousAlliances() {
        return mostCuriousAlliances;
    }

    public ArrayList<Triplet> getMostCuriousPlayers() {
        return mostCuriousPlayers;
    }

    public HighScore getHighscoreFromPlayername(String name) {
        return highscores.get(name);
    }

    public HighScore getHighscoreFromAllyname(String name) {
        return highscoresAlliance.get(name);
    }

    public String getValueJSON(JSONArray array, int position) throws JSONException {
        return array.isNull(position) ? "0" : array.getString(position);
    }

    public int getIntValueJSON(JSONArray array, int position) throws JSONException {
        return array.isNull(position) ? 0 : array.getInt(position);
    }
}
