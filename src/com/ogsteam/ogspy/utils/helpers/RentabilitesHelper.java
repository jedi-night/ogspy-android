package com.ogsteam.ogspy.utils.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
                        rentabilites.put(renta.getString(0), new Rentabilite(renta.getString(0), renta.getInt(1), renta.getInt(2), renta.getInt(3), renta.getInt(4), renta.getInt(5)));
                    }

                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluation des donnees Rentabilites");
        }
    }

    public class Rentabilite implements Comparable<Rentabilite> {
        private String user;
        private int metal;
        private int cristal;
        private int deuterium;
        private int pertes;
        private int gains;

        public Rentabilite() {
            this.user = "";
            this.metal = 0;
            this.cristal = 0;
            this.deuterium = 0;
            this.pertes = 0;
            this.gains = 0;
        }

        public Rentabilite(int metal, int cristal, int deuterium, int pertes, int gains) {
            this.user = "";
            this.metal=metal;
            this.cristal=cristal;
            this.deuterium = deuterium;
            this.pertes = pertes;
            this.gains = gains;
        }

        public Rentabilite(String user, int metal, int cristal, int deuterium, int pertes, int gains) {
            this.user = user;
            this.metal = metal;
            this.cristal = cristal;
            this.deuterium = deuterium;
            this.pertes = pertes;
            this.gains = gains;
        }

        public String getMetal() {
            return String.valueOf(metal);
        }

        public int getMetalInt() {
            return metal;
        }

        public String getCristal() {
            return String.valueOf(cristal);
        }

        public int getCristalInt() {
            return cristal;
        }

        public String getDeuterium() {
            return String.valueOf(deuterium);
        }

        public int getDeuteriumInt() {
            return deuterium;
        }

        public String getPertes() {
            return String.valueOf(pertes);
        }

        public int getPertesInt() {
            return pertes;
        }

        public String getGains() {
            return String.valueOf(gains);
        }

        public int getGainsInt() {
            return gains;
        }

        public String getUser() {
            return user;
        }

        public void addRenta(int metal, int cristal, int deuterium, int pertes, int gains) {
            this.metal += metal;
            this.cristal += cristal;
            this.deuterium += deuterium;
            this.pertes += pertes;
            this.metal += gains;
        }

        @Override
        public int compareTo(Rentabilite rentabilite) {
            if (this.gains > rentabilite.gains) { // pour le graphique, on fait un tri descendant si > alors <
                return -1;
            } else if (this.gains < rentabilite.gains) {
                return 1;
            }
            return 0;
        }
    }

    public TreeMap<String, Rentabilite> getRentabilites() {
        return rentabilites;
    }

    public ArrayList<Rentabilite> getRentabilitesSortedByGains() {
        ArrayList<Rentabilite> rentas = new ArrayList<Rentabilite>();
        rentas.addAll(rentabilites.values());
        Collections.sort(rentas);
        return rentas;
    }

    public Rentabilite getTotalRentabilite() {
        Rentabilite rentabiliteTotale = new Rentabilite();
        for (Iterator<String> iterator = rentabilites.keySet().iterator(); iterator.hasNext(); ) {
            String user = iterator.next();
            Rentabilite rentabilite = rentabilites.get(user);
            rentabiliteTotale.addRenta(rentabilite.getMetalInt(), rentabilite.getCristalInt(), rentabilite.getDeuteriumInt(), rentabilite.getPertesInt(), rentabilite.getGainsInt());
        }
        return rentabiliteTotale;
    }
}
