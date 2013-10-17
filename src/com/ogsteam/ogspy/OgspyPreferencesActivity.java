package com.ogsteam.ogspy;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.preferences.Accounts;
import com.ogsteam.ogspy.preferences.Preferences;
import com.ogsteam.ogspy.ui.DialogHandler;

/**
 * Created by jp.tessier on 16/07/13.
 */
public class OgspyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.prefs);
        Accounts.showAccount(OgspyActivity.activity, PreferenceManager.getDefaultSharedPreferences(this));
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onDestroy() {
        try {
            Accounts.saveAccount(OgspyActivity.activity, PreferenceManager.getDefaultSharedPreferences(this));
        } catch (OgspyException ogse){
            new DialogHandler().showException(OgspyActivity.activity, ogse);
        }
        try {
            Preferences.savePrefs(OgspyActivity.activity, PreferenceManager.getDefaultSharedPreferences(this));
        } catch (OgspyException ogse){
            new DialogHandler().showException(OgspyActivity.activity, ogse);
        }
        CommonUtilities.displayMessage(OgspyActivity.activity, "Préférences enregistrées");
        super.onDestroy();
    }
}
