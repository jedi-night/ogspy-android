package com.ogsteam.ogspy.server;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by Jedinight on 04/06/13.
 */
public class HostilesHelper {
    private boolean isAttack = false;
    private TreeMap<String,ArrayList<Cible>> attaques;
    private TreeMap<String,ArrayList<Cible>> attaquesGroup;

    public HostilesHelper(JSONObject datas){
        attaques = new TreeMap<String, ArrayList<Cible>>();
        try{
            // "hostile": {"isAttack": 1,"attaks": [[[[[0,"Test","Toto","1:1:1","Tata","9:9:9"]]]]]}
            JSONObject hostile = datas.getJSONObject("hostile");
            if(hostile!=null && hostile.length() > 0){
                isAttack = "1".equals(hostile.getString("isAttack"));
                if(isAttack){
                    JSONArray attaks = hostile.getJSONArray("attaks");
                    for(int i=0;i < attaks.length(); i++){
                        JSONArray attack = attaks.getJSONArray(i);
                        JSONArray compos = attack.getJSONArray(9);
                        Cible cible = new Cible(attack.getString(0),attack.getString(2),attack.getString(3),attack.getString(4),attack.getString(5),attack.getString(6),attack.getString(7),attack.getString(8),compos);
                        if("0".equals(attack.getString(2))){ // Vague = 0 : Attaque simple
                            String attackUser = attack.getString(1);
                            if(!attaques.containsKey(attackUser)){
                                ArrayList<Cible> array = new ArrayList<Cible>();
                                array.add(cible);
                                attaques.put(attackUser, array);
                            } else {
                                attaques.get(attackUser).add(cible);
                            }
                        } else { // Attaque groupée
                            // TODO : gérer les attaques groupées
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(),"Probleme lors de l'evaluation des donnees Hostiles");
        }
    }

    public boolean isAttack() {
        return isAttack;
    }

    public TreeMap<String, ArrayList<Cible>> getAttaques() {
        return attaques;
    }

    public class Cible {
        private String id;
        private String vague;
        private String attacker;
        private String originPlanet;
        private String originCoords;
        private String ciblePlanet;
        private String cibleCoords;
        private String arrivalTime;
        private TreeMap<String,String> compos;

        public Cible(String id, String vague, String attacker, String originPlanet, String originCoords, String ciblePlanet, String cibleCoords, String arrivalTime, JSONArray compos) throws JSONException {
            this.id = id;
            this.vague = vague;
            this.attacker = attacker;
            this.originPlanet = originPlanet;
            this.originCoords = originCoords;
            this.ciblePlanet = ciblePlanet;
            this.cibleCoords = cibleCoords;
            this.arrivalTime = arrivalTime;
            this.compos = getCompos(compos);
        }

        private TreeMap<String,String> getCompos(JSONArray flottes) throws JSONException {
            TreeMap<String,String> retour = new TreeMap<String, String>();
            for(int i=0;i<flottes.length();i++){
                JSONArray flotte = flottes.getJSONArray(i);
                retour.put(flotte.getString(0),flotte.getString(1));
            }
            return retour;
        }

        public String getId() {
            return id;
        }

        public String getVague() {
            return vague;
        }

        public String getAttacker() {
            return attacker;
        }

        public String getOriginPlanet() {
            return originPlanet;
        }

        public String getOriginCoords() {
            return originCoords;
        }

        public String getCiblePlanet() {
            return ciblePlanet;
        }

        public String getCibleCoords() {
            return cibleCoords;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public String getCompo(){
            StringBuffer sb = new StringBuffer();
            for(Iterator<String> compo = compos.keySet().iterator(); compo.hasNext(); ){
                String comp = compo.next();
                sb.append(comp).append(" : ").append(compos.get(comp));
                if(compo.hasNext()){
                    sb.append(" - ");
                }
            }
            return sb.toString();
        }
    }
}
