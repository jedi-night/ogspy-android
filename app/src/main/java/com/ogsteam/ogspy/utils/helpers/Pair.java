package com.ogsteam.ogspy.utils.helpers;

/**
 * Created by jp.tessier on 12/07/13.
 */
public class Pair {
    public String key;
    public int keyInt;
    public String value;
    public int valueInt;

    public Pair(String key, String value){
        this.key =key;
        this.value = value;
    }

    public Pair(int key, String value){
        this.keyInt =key;
        this.value = value;
    }

    public Pair(int key, int value) {
        this.keyInt = key;
        this.valueInt = value;
    }
}
