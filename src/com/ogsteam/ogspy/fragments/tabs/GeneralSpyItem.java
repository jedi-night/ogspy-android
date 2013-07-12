package com.ogsteam.ogspy.fragments.tabs;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class GeneralSpyItem {
    private String name;
    private String number;

    public GeneralSpyItem(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
