package com.ogsteam.ogspy.utils.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jp.tessier on 17/01/14.
 */

public class HighScore implements Parcelable {
    private String rankGeneral, rankMilitary, rankResearch, rankEconomic;
    private int ptsGeneral, ptsMilitary, ptsResearch, ptsEconomic;

    public static final Parcelable.Creator<HighScore> CREATOR = new Parcelable.Creator<HighScore>() {
        @Override
        public HighScore createFromParcel(Parcel source) {
            return new HighScore(source);
        }

        @Override
        public HighScore[] newArray(int size) {
            return new HighScore[size];
        }
    };

    public HighScore(String rankGeneral, String rankMilitary, String rankResearch, String rankEconomic, int ptsGeneral, int ptsMilitary, int ptsResearch, int ptsEconomic) {
        this.rankGeneral = rankGeneral;
        this.rankMilitary = rankMilitary;
        this.rankResearch = rankResearch;
        this.rankEconomic = rankEconomic;
        this.ptsGeneral = ptsGeneral;
        this.ptsMilitary = ptsMilitary;
        this.ptsResearch = ptsResearch;
        this.ptsEconomic = ptsEconomic;
    }

    public HighScore(Parcel in) {
        this.rankGeneral = in.readString();
        this.rankMilitary = in.readString();
        this.rankResearch = in.readString();
        this.rankEconomic = in.readString();
        this.ptsGeneral = in.readInt();
        this.ptsMilitary = in.readInt();
        this.ptsResearch = in.readInt();
        this.ptsEconomic = in.readInt();
    }

    public Parcelable.Creator<HighScore> getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rankGeneral);
        dest.writeString(rankMilitary);
        dest.writeString(rankResearch);
        dest.writeString(rankEconomic);
        dest.writeInt(ptsGeneral);
        dest.writeInt(ptsMilitary);
        dest.writeInt(ptsResearch);
        dest.writeInt(ptsEconomic);
    }

    public String getRankGeneral() {
        return rankGeneral;
    }

    public String getRankMilitary() {
        return rankMilitary;
    }

    public String getRankResearch() {
        return rankResearch;
    }

    public String getRankEconomic() {
        return rankEconomic;
    }

    public int getPtsGeneral() {
        return ptsGeneral;
    }

    public int getPtsMilitary() {
        return ptsMilitary;
    }

    public int getPtsResearch() {
        return ptsResearch;
    }

    public int getPtsEconomic() {
        return ptsEconomic;
    }

    public void setRankGeneral(String rankGeneral) {
        this.rankGeneral = rankGeneral;
    }

    public void setRankMilitary(String rankMilitary) {
        this.rankMilitary = rankMilitary;
    }

    public void setRankResearch(String rankResearch) {
        this.rankResearch = rankResearch;
    }

    public void setRankEconomic(String rankEconomic) {
        this.rankEconomic = rankEconomic;
    }

    public void setPtsGeneral(int ptsGeneral) {
        this.ptsGeneral = ptsGeneral;
    }

    public void setPtsMilitary(int ptsMilitary) {
        this.ptsMilitary = ptsMilitary;
    }

    public void setPtsResearch(int ptsResearch) {
        this.ptsResearch = ptsResearch;
    }

    public void setPtsEconomic(int ptsEconomic) {
        this.ptsEconomic = ptsEconomic;
    }

    @Override
    public String toString() {
        return "HighScore{" +
                "rankGeneral='" + rankGeneral + '\'' +
                ", rankMilitary='" + rankMilitary + '\'' +
                ", rankResearch='" + rankResearch + '\'' +
                ", rankEconomic='" + rankEconomic + '\'' +
                ", ptsGeneral=" + ptsGeneral +
                ", ptsMilitary=" + ptsMilitary +
                ", ptsResearch=" + ptsResearch +
                ", ptsEconomic=" + ptsEconomic +
                '}';
    }

}
