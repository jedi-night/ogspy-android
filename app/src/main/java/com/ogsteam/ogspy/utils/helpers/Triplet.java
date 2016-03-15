package com.ogsteam.ogspy.utils.helpers;

/**
 * Created by jp.tessier on 17/01/14.
 */
public class Triplet {
    public String key;
    public String keyLibelle;
    public int keyInt;
    public String value;

    public Triplet(String key, String keyLibelle, String value) {
        this.key = key;
        this.keyLibelle = keyLibelle;
        this.value = value;
    }

    public Triplet(int key, String keyLibelle, String value) {
        this.keyInt = key;
        this.keyLibelle = keyLibelle;
        this.value = value;
    }

}
