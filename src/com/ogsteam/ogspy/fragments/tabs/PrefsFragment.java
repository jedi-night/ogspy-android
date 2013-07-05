package com.ogsteam.ogspy.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.preferences.Preferences;


/**
 * @author mwho
 *
 */
public class PrefsFragment extends Fragment {
    private static Spinner timerHostiles;
    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.prefs, container, false);

        timerHostiles = (Spinner) layout.findViewById(R.id.prefs_timer_hostiles);

        Preferences.showPrefs((OgspyActivity) getActivity(), this);

        return layout;
    }

    public static Spinner getTimerHostiles() {
        return timerHostiles;
    }
}
