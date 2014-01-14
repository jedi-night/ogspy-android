package com.ogsteam.ogspy.fragments.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.ui.displays.GeneralUtils;


/**
 * @author Jedinight
 */
public class GeneralFragment extends Fragment {
    private static ScrollView layout;
    private static TextView allianceOwn;
    private static TextView nbMembers;
    private static ListView mostCuriousAlliances;
    private static ListView mostCuriousPlayers;

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
        layout = (ScrollView) inflater.inflate(R.layout.general, container, false);

        allianceOwn = (TextView) layout.findViewById(R.id.alliance);
        nbMembers = (TextView) layout.findViewById(R.id.nb_members);
        mostCuriousAlliances = (ListView) layout.findViewById(R.id.curiousAlliances);
        mostCuriousPlayers = (ListView) layout.findViewById(R.id.curiousPlayers);

        if (OgspyActivity.activity.getDownloadSpysTask() != null && OgspyActivity.activity.getDownloadSpysTask().getSpysHelper() == null) {
            DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadSpysTask);
        } else {
            GeneralUtils.showGeneral(null, null, OgspyActivity.activity.getDownloadSpysTask().getSpysHelper(), OgspyActivity.activity);
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (OgspyActivity.getDownloadAllianceTask() != null && OgspyActivity.getDownloadAllianceTask().getAllianceHelper() != null) {
            GeneralUtils.showGeneral(null, OgspyActivity.getDownloadAllianceTask().getAllianceHelper(), null, (OgspyActivity) getActivity());
        } else {
            new DownloadAllianceTask((OgspyActivity) getActivity()).execute(new String[]{"do"});
        }
        if (OgspyActivity.getDownloadSpysTask() != null && OgspyActivity.getDownloadSpysTask().getSpysHelper() != null) {
            GeneralUtils.showGeneral(null, null, OgspyActivity.getDownloadSpysTask().getSpysHelper(), (OgspyActivity) getActivity());
        } else {
            new DownloadSpysTask((OgspyActivity) getActivity()).execute(new String[]{"do"});
        }*/
    }

    public static TextView getAllianceOwn() {
        return allianceOwn;
    }

    public static TextView getNbMembers() {
        return nbMembers;
    }

    public static ListView getMostCuriousAlliances() {
        return mostCuriousAlliances;
    }

    public static ListView getMostCuriousPlayers() {
        return mostCuriousPlayers;
    }

}
