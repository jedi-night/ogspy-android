package com.ogsteam.ogspy.fragments.tabs.items;

import com.ogsteam.ogspy.utils.helpers.HostilesHelper;

import java.util.ArrayList;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class HostileItem {

    private int image;
    private String title;
    private String date;
    private String detail;
    private static ArrayList<String> detailsAg;
    private boolean isAg;
    private String compo;

    private HostilesHelper.Cible cible;
    HostilesHelper.AG ag;

        public HostileItem(boolean isAg){
            this.isAg=isAg;
            this.detailsAg = new ArrayList<String>();
        }

    public boolean isAg() {
        return isAg;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

        public String getTitle() {
            return title;
        }

        public void setTitle(String cible, String ciblePlanet, String cibleCoords) {
            this.title = cible + " -  " + ciblePlanet + " (" + cibleCoords + ")";
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
            StringBuffer retour = new StringBuffer(attaquant).append(" - ").append(originPlanet).append(" (").append(originCoords).append(")");
            if(isAg){
                StringBuffer retourAg = new StringBuffer(retour);
                detailsAg.add(retourAg.append(" :\n\n").append(compo).toString());
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

    public void setCible(HostilesHelper.Cible cible) {
        this.cible = cible;
    }

    public void setAg(HostilesHelper.AG ag) {
        this.ag = ag;
    }

    public HostilesHelper.AG getAg() {
        return ag;
    }

    public HostilesHelper.Cible getCible() {
        return cible;
    }

    @Override
        public String toString() {
            StringBuffer retour = new StringBuffer(detail).append(" :\n\n").append(compo);
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
