package com.ogsteam.ogspy.fragments.tabs;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadRentabilitesTask;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.ui.displays.RentabilitesUtils;

/**
 * @author mwho
 */
public class RentabilitesFragment extends Fragment {
    private static Spinner rentabiliteInterval;
    private static Spinner rentabiliteType;
    private static TextView rentabilityTotal;
    private static RelativeLayout pieChartContainer;

    private static int lastSeletedTypePosition = -1;
    private static int lastSeletedIntervalPosition = -1;

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

        ScrollView layout = (ScrollView) inflater.inflate(R.layout.rentabilite, container, false);

        pieChartContainer = (RelativeLayout) layout.findViewById(R.id.pieChartContainer);

        rentabilityTotal = (TextView) layout.findViewById(R.id.rentabilityTotal);

        //activity.setContentView(R.layout.prefs);
        rentabiliteInterval = (Spinner) layout.findViewById(R.id.rentas_interval);
        rentabiliteType = (Spinner) layout.findViewById(R.id.rentas_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterInterval = ArrayAdapter.createFromResource(OgspyActivity.activity, R.array.interval, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(OgspyActivity.activity, R.array.rentas_type, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (rentabiliteInterval != null && adapterInterval != null) {
            rentabiliteInterval.setAdapter(adapterInterval);
        }
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (rentabiliteType != null && adapterType != null) {
            rentabiliteType.setAdapter(adapterType);
        }

        int interval = 0;
        int type = 0;
        if (lastSeletedTypePosition == -1 && lastSeletedIntervalPosition == -1) {
            interval = getIntervalPositionFromPrefs();
            type = getTypePositionFromPrefs();
            rentabiliteInterval.setSelection(interval);
            rentabiliteType.setSelection(type);
            executeDownload(interval, type);
            lastSeletedIntervalPosition = interval;
            lastSeletedTypePosition = type;
        } else {
            interval = lastSeletedIntervalPosition == -1 ? 0 : lastSeletedIntervalPosition;
            type = lastSeletedTypePosition == -1 ? 0 : lastSeletedTypePosition;
            rentabiliteInterval.setSelection(interval);
            rentabiliteType.setSelection(type);
        }

        rentabiliteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (lastSeletedTypePosition >= 0 && lastSeletedTypePosition != position) {
                    executeDownload();
                } else {
                    RentabilitesUtils.showRentabilites(OgspyActivity.activity.getDownloadRentasTask().getHelperRentabilites(), OgspyActivity.activity, OgspyActivity.activity.getDownloadRentasTask().getType());
                }
                lastSeletedTypePosition = position;
                //DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadRentasTask);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        rentabiliteInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (lastSeletedIntervalPosition >= 0 && lastSeletedIntervalPosition != position) {
                    executeDownload();
                } else {
                    RentabilitesUtils.showRentabilites(OgspyActivity.activity.getDownloadRentasTask().getHelperRentabilites(), OgspyActivity.activity, OgspyActivity.activity.getDownloadRentasTask().getType());
                }
                lastSeletedIntervalPosition = position;
                //DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadRentasTask);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        return layout;
    }

    public void executeDownload() {
        DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadRentasTask);
    }

    public void executeDownload(int interval, int type) {
        OgspyActivity.activity.downloadRentasTask = new DownloadRentabilitesTask(OgspyActivity.activity, getIntervalFromSelectedPosition(interval), getTypeFromSelectedPosition(type));
        DownloadTask.executeDownload(OgspyActivity.activity, OgspyActivity.activity.downloadRentasTask);
    }

    public static RelativeLayout getPieChartContainer() {
        return pieChartContainer;
    }

    public static Spinner getRentabiliteInterval() {
        return rentabiliteInterval;
    }

    public static TextView getRentabilityTotal() {
        return rentabilityTotal;
    }

    public static Spinner getRentabiliteType() {
        return rentabiliteType;
    }

    public int getIntervalPositionFromPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.contains("prefs_rentas_interval")) {
            String interval = preferences.getString("prefs_rentas_interval", "");
            String[] intervals = getResources().getStringArray(R.array.interval_values);

            for (int i = 0; i < intervals.length; i++) {
                if (intervals[i].equals(interval)) {
                    return i;
                }
            }
            return 0;
        } else {
            return 0;
        }
    }


    public int getTypePositionFromPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.contains("prefs_rentas_type")) {
            String type = preferences.getString("prefs_rentas_type", "");
            String[] types = getResources().getStringArray(R.array.rentas_type_values);

            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(type)) return i;
            }
            return 0;
        } else {
            return 0;
        }
    }

    private String getTypeFromSelectedPosition(int positionSelectedType) {
        return OgspyActivity.activity.getResources().getStringArray(R.array.rentas_type_values)[positionSelectedType];
    }

    private String getIntervalFromSelectedPosition(int positionSelected) {
        return OgspyActivity.activity.getResources().getStringArray(R.array.interval_values)[positionSelected];
    }


}
