package com.ogsteam.ogspy.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadHostilesTask;


/**
 * @author mwho
 */
public class HostileFragment extends Fragment {
    private static ScrollView layout;
    private static ListView listHostiles;

    /**
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        layout = (ScrollView) inflater.inflate(R.layout.hostiles, container, false);

        listHostiles = (ListView) layout.findViewById(R.id.list_view_hostiles);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        new DownloadHostilesTask((OgspyActivity) getActivity()).execute(new String[]{"do"});
    }

    public static ListView getListHostiles() {
        return listHostiles;
    }
}
