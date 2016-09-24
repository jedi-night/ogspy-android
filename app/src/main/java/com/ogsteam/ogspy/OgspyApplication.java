package com.ogsteam.ogspy;

import android.app.Application;
import android.content.Context;

/**
 * Created by Damien on 24/09/2016.
 */

public class OgspyApplication extends Application {

    private static OgspyApplication sApplication;
    @Override
    public void onCreate() {
        sApplication = this;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }
}
