package com.ogsteam.ogspy.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.TreeMap;

/**
 * Created by Jedinight on 04/06/13.
 */
public class RentabilitesHelper {
    private TreeMap<String,Rentabilite> rentabilites; // Sort by user

    public RentabilitesHelper(JSONObject datas){
        rentabilites = new TreeMap<String, Rentabilite>();
        try{
            // "hostile": {"isAttack": 1,"attaks": [[[[[0,"Test","Toto","1:1:1","Tata","9:9:9"]]]]]}
            if(datas.has("rentasPlayers")){
                JSONArray rentas = datas.getJSONArray("rentasPlayers");
                if(rentas!=null && rentas.length() > 0){
                    for(int i=0;i < rentas.length(); i++){
                        JSONArray renta = rentas.getJSONArray(i);
                        rentabilites.put(renta.getString(0),new Rentabilite(renta.getString(1),renta.getString(2),renta.getString(3),renta.getString(4),renta.getString(5)));
                    }

                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluation des donnees Rentabilites");
        }
    }

    public class Rentabilite {
        private String metal;
        private String cristal;
        private String deuterium;
        private String pertes;
        private String gains;

        public Rentabilite(String metal,String cristal, String deuterium, String pertes, String gains) {
            this.metal=metal;
            this.cristal=cristal;
            this.deuterium = deuterium;
            this.pertes = pertes;
            this.gains = gains;
        }

        public String getMetal() {
            return metal;
        }

        public String getCristal() {
            return cristal;
        }

        public String getDeuterium() {
            return deuterium;
        }

        public String getPertes() {
            return pertes;
        }

        public String getGains() {
            return gains;
        }
    }

    public TreeMap<String, Rentabilite> getRentabilites() {
        return rentabilites;
    }
}
