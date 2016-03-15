package com.ogsteam.ogspy.fragments.tabs.items;

import com.ogsteam.ogspy.utils.objects.HighScore;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class GeneralSpyItem {
    private String name;
    private String shortName;
    private String number;
    private HighScore highscore;

    public GeneralSpyItem(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public GeneralSpyItem(String name, String number, HighScore highscore, String shortName) {
        this.name = name;
        this.shortName = shortName;
        this.number = number;
        this.highscore = highscore;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getNumber() {
        return number;
    }

    public HighScore getHighscore() {
        return highscore;
    }

    @Override
    public String toString() {
        return "GeneralSpyItem{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", highscore=" + highscore.getRankGeneral() +
                '}';
    }
}
