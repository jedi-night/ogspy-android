package com.ogsteam.ogspy.fragments.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadSpysTask;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.ui.displays.SpysUtils;


/**
 * @author Jedinight
 */
public class SpysFragment extends Fragment {
    private static ScrollView layout;

    private static Spinner rentabiliteInterval;
    private static ListView mostCuriousAlliances;
    private static ListView mostCuriousPlayers;

    private static int lastSeletedIntervalPosition = -1;

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
        layout = (ScrollView) inflater.inflate(R.layout.spys, container, false);
        rentabiliteInterval = (Spinner) layout.findViewById(R.id.spys_interval);

        ArrayAdapter<CharSequence> adapterInterval = ArrayAdapter.createFromResource(OgspyActivity.activity, R.array.interval, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (rentabiliteInterval != null && adapterInterval != null) {
            rentabiliteInterval.setAdapter(adapterInterval);
        }

        int interval = 3; // month
        if (lastSeletedIntervalPosition == -1) {
            //interval = getIntervalPositionFromPrefs();
            rentabiliteInterval.setSelection(interval);
            executeDownload(interval);
            lastSeletedIntervalPosition = interval;
        } else {
            interval = lastSeletedIntervalPosition == -1 ? 3 : lastSeletedIntervalPosition;
            rentabiliteInterval.setSelection(interval);
        }

        rentabiliteInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (lastSeletedIntervalPosition >= 0 && lastSeletedIntervalPosition != position) {
                    executeDownload();
                } else {
                    SpysUtils.showSpys(null, null, OgspyActivity.activity.getDownloadSpysTask().getSpysHelper(), OgspyActivity.activity);
                }
                lastSeletedIntervalPosition = position;
                //DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadRentasTask);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        mostCuriousAlliances = (ListView) layout.findViewById(R.id.curiousAlliances);
        mostCuriousPlayers = (ListView) layout.findViewById(R.id.curiousPlayers);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (OgspyActivity.getDownloadAllianceTask() != null && OgspyActivity.getDownloadAllianceTask().getAllianceHelper() != null) {
            SpysUtils.showGeneral(null, OgspyActivity.getDownloadAllianceTask().getAllianceHelper(), null, (OgspyActivity) getActivity());
        } else {
            new DownloadAllianceTask((OgspyActivity) getActivity()).execute(new String[]{"do"});
        }
        if (OgspyActivity.getDownloadSpysTask() != null && OgspyActivity.getDownloadSpysTask().getSpysHelper() != null) {
            SpysUtils.showGeneral(null, null, OgspyActivity.getDownloadSpysTask().getSpysHelper(), (OgspyActivity) getActivity());
        } else {
            new DownloadSpysTask((OgspyActivity) getActivity()).execute(new String[]{"do"});
        }*/
    }

    public void executeDownload() {
        DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadSpysTask);
    }

    public void executeDownload(int interval) {
        OgspyActivity.activity.downloadSpysTask = new DownloadSpysTask(OgspyActivity.activity, getIntervalFromSelectedPosition(interval));
        DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadSpysTask);
    }

    public static Spinner getInterval() {
        return rentabiliteInterval;
    }

    public static ListView getMostCuriousAlliances() {
        return mostCuriousAlliances;
    }

    public static ListView getMostCuriousPlayers() {
        return mostCuriousPlayers;
    }

    private String getIntervalFromSelectedPosition(int positionSelected) {
        return OgspyActivity.activity.getResources().getStringArray(R.array.interval_values)[positionSelected];
    }
}
