package com.ogsteam.ogspy;

import com.ogsteam.ogspy.utils.Pair;

/**
 * Created by jp.tessier on 19/07/13.
 */
public class OgspyException extends Exception {
    private Pair typeException;

    /**
     *
     * @param message : détail du problème
     * @param typeException : type du problème
     */
    public OgspyException(String message, Pair typeException) {
        super(message);
        this.typeException = typeException;
    }

    public Pair getTypeException() {
        return typeException;
    }
}
