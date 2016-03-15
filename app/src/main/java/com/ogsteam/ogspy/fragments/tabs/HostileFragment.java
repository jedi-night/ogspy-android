package com.ogsteam.ogspy.fragments.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.ui.displays.HostileUtils;


/**
 * @author mwho
 */
public class HostileFragment extends Fragment {
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.hostiles, container, false);

        listHostiles = (ListView) layout.findViewById(R.id.list_view_hostiles);

        if (OgspyActivity.activity.getDownloadHostilesTask() != null && OgspyActivity.activity.getDownloadHostilesTask().getHelperHostile() == null) {
            DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadHostilesTask);
        } else {
            HostileUtils.showHostiles(OgspyActivity.activity.getDownloadHostilesTask().getHelperHostile(), OgspyActivity.activity);
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        //new DownloadHostilesTask((OgspyActivity) getActivity()).execute(new String[]{"do"});
    }

    public static ListView getListHostiles() {
        return listHostiles;
    }
}
