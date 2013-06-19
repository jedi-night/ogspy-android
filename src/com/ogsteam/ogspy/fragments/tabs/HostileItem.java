package com.ogsteam.ogspy.fragments.tabs;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class HostileItem {
    private String image;
    private String title;
    private String date;
    private String detail;

    private String compo;

        public HostileItem(){

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

        public void setDetail(String originPlanet, String originCoords, String attaquant) {
            this.detail = attaquant + " de " + originPlanet  +" (" + originCoords + ")";
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
            return new StringBuffer(detail).append(" attaque avec ").append(compo).toString();
        }
}
