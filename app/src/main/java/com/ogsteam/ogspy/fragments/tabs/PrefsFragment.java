package com.ogsteam.ogspy.fragments.tabs;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ogsteam.ogspy.R;

/**
 * Created by jp.tessier on 16/12/13.
 */
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
