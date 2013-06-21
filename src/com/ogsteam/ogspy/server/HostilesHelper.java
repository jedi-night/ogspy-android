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
    private TreeMap<String,ArrayList<Cible>> attaques; // Sort by user
    private TreeMap<String,TreeMap<String,AG>> attaquesGroup; // Sort by id_attack

    public HostilesHelper(JSONObject datas){
        attaques = new TreeMap<String, ArrayList<Cible>>();
        attaquesGroup = new TreeMap<String, TreeMap<String,AG>>();
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
                        if("0".equals(attack.getString(2))){ // Vague = 0 : Attaque simple
                            String attackUser = attack.getString(1);
                            Cible cible = new Cible(attack.getString(0),attack.getString(2),attack.getString(3),attack.getString(4),attack.getString(5),attack.getString(6),attack.getString(7),attack.getString(8),compos);
                            if(!attaques.containsKey(attackUser)){
                                ArrayList<Cible> array = new ArrayList<Cible>();
                                array.add(cible);
                                attaques.put(attackUser, array);
                            } else {
                                attaques.get(attackUser).add(cible);
                            }
                        } else { // Attaque groupée
                            String idAttack = attack.getString(0);
                            AG ag = new AG(attack.getString(0),attack.getString(1),attack.getString(6),attack.getString(7),attack.getString(8));
                            Vague vague = new Vague(attack.getString(2),attack.getString(3),attack.getString(4),attack.getString(5),compos);
                            if(!attaquesGroup.containsKey(idAttack)){
                                TreeMap<String,AG> array = new TreeMap<String,AG>();
                                ag.addVague(vague);
                                array.put(idAttack, ag);
                                attaquesGroup.put(idAttack, array);
                            } else {
                                attaquesGroup.get(idAttack).get(idAttack).addVague(vague);
                            }
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

    public TreeMap<String, TreeMap<String, AG>> getAttaquesGroup() {
        return attaquesGroup;
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

    public class AG {
        private String id;
        private String cible;
        private String ciblePlanet;
        private String cibleCoords;
        private String arrivalTime;
        private TreeMap<String,Vague> vagues;

        public AG(String id, String cible, String ciblePlanet, String cibleCoords, String arrivalTime) throws JSONException {
            this.id = id;
            this.cible = cible;
            this.ciblePlanet = ciblePlanet;
            this.cibleCoords = cibleCoords;
            this.arrivalTime = arrivalTime;
            vagues = new TreeMap<String, Vague>();
        }

        public String getId() {
            return id;
        }

        public String getCible() {
            return cible;
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

        public void addVague(Vague vague) {
            vagues.put(vague.getVague(), vague);
        }

        public TreeMap<String, Vague> getVagues() {
            return vagues;
        }

    }

    public class Vague {
        private String vague;
        private String attacker;
        private String originPlanet;
        private String originCoords;
        private TreeMap<String,String> compos;

        public  Vague(String vague,String attacker, String originPlanet, String originCoords, JSONArray compos) throws JSONException {
            this.vague=vague;
            this.attacker=attacker;
            this.originPlanet = originPlanet;
            this.originCoords = originCoords;
            this.compos = getCompos(compos);
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
    }

    private TreeMap<String,String> getCompos(JSONArray flottes) throws JSONException {
        TreeMap<String,String> retour = new TreeMap<String, String>();
        for(int i=0;i<flottes.length();i++){
            JSONArray flotte = flottes.getJSONArray(i);
            retour.put(flotte.getString(0),flotte.getString(1));
        }
        return retour;
    }
}
