package com.ogsteam.ogspy.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by jp.tessier on 29/01/14.
 */
public class ListPreferenceShowSummary extends ListPreference {

    private final static String TAG = ListPreferenceShowSummary.class.getName();

    public ListPreferenceShowSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListPreferenceShowSummary(Context context) {
        super(context);
        init();
    }

    private void init() {

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference arg0, Object arg1) {
                arg0.setSummary(getEntry());
                return true;
            }
        });
    }

    @Override
    public CharSequence getSummary() {
        return super.getEntry();
    }
}