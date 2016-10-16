package com.ogsteam.ogspy.network.download;

import java.util.HashMap;

/**
 * Created by jpspa on 16/10/2016.
 */

public class UrlWithParameters {
    private String url;
    HashMap<String, String> parameters;

    public UrlWithParameters(String url) {
        this.url = url;
        this.parameters =  new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }
}
