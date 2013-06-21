package com.ogsteam.ogspy.fragments.tabs;

import java.util.ArrayList;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class HostileItem {
    private String title;
    private String date;
    private String detail;
    private static ArrayList<String> detailsAg;
    private boolean isAg;
    private String compo;

        public HostileItem(boolean isAg){
            this.isAg=isAg;
            this.detailsAg = new ArrayList<String>();
        }

    public boolean isAg() {
        return isAg;
    }

        public String getTitle() {
            return title;
        }

        public void setTitle(String cible, String ciblePlanet, String cibleCoords) {
            this.title = cible + " est attaqué sur  " + ciblePlanet  +" (" + cibleCoords + ")";
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail=detail;
        }

        public void setDetail(String originPlanet, String originCoords, String attaquant,boolean isAg,String compo) {
            this.detail = getDetail(originPlanet,originCoords,attaquant,isAg,compo);
        }

        public static String getDetail(String originPlanet, String originCoords, String attaquant,boolean isAg,String compo) {
            StringBuffer retour = new StringBuffer(attaquant).append(" de ").append(originPlanet).append(" (").append(originCoords).append(")");
            if(isAg){
                StringBuffer retourAg = new StringBuffer(retour);
                detailsAg.add(retourAg.append(" attaque avec \n").append(compo).toString());
            }
            return retour.toString();
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    public void setCompo(String compo) {
        this.compo = compo;
    }

        @Override
        public String toString() {
            StringBuffer retour = new StringBuffer(detail).append(" attaque avec \n").append(compo);
            if(this.isAg()){
                retour = new StringBuffer();
                int i = 0;
                for(String thisdetail:detailsAg){
                    retour.append(thisdetail);
                    if(i<detailsAg.size()){
                        retour.append("\n\n");
                    }
                }
            }
            return retour.toString();
        }
}
