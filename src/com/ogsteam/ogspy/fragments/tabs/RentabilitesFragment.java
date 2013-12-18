package com.ogsteam.ogspy.fragments.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadRentabilitesTask;

/**
 * @author mwho
 */
public class RentabilitesFragment extends Fragment {
    private static Spinner rentabiliteInterval;
    private static Spinner rentabiliteType;
    private static TextView rentabilityTotal;
    private static LinearLayout pieChartContainer;

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

        TableLayout layout = (TableLayout) inflater.inflate(R.layout.rentabilite, container, false);

        pieChartContainer = (LinearLayout) layout.findViewById(R.id.pieChartContainer);

        rentabilityTotal = (TextView) layout.findViewById(R.id.rentabilityTotal);

        //activity.setContentView(R.layout.prefs);
        rentabiliteInterval = (Spinner) layout.findViewById(R.id.rentas_interval);
        rentabiliteType = (Spinner) layout.findViewById(R.id.rentas_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterInterval = ArrayAdapter.createFromResource(OgspyActivity.activity, R.array.rentas_interval, android.R.layout.simple_spinner_item);
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

        rentabiliteInterval.setSelection(0);
        rentabiliteType.setSelection(0);

        rentabiliteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positionSelected = getRentabiliteInterval().getSelectedItemPosition();
                int positionSelectedType = getRentabiliteType().getSelectedItemPosition();
                Log.d(RentabilitesFragment.class.getName(), "OnItemSelectedListener de rentabiliteType ! posPeriode=" + positionSelected + " posType=" + positionSelectedType);
                String interval = getResources().getStringArray(R.array.rentas_interval_values)[positionSelected];
                String type = getResources().getStringArray(R.array.rentas_type_values)[positionSelectedType];
                if (OgspyActivity.activity.downloadRentasTask == null || OgspyActivity.activity.downloadRentasTask.canExecute()) {
                    DownloadRentabilitesTask rentasTask = new DownloadRentabilitesTask(OgspyActivity.activity, interval, type);
                    rentasTask.execute(new String[]{"do"});
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        rentabiliteInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positionSelected = getRentabiliteInterval().getSelectedItemPosition();
                int positionSelectedType = getRentabiliteType().getSelectedItemPosition();
                Log.d(RentabilitesFragment.class.getName(), "OnItemSelectedListener de rentabiliteInterval ! posPeriode=" + positionSelected + " posType=" + positionSelectedType);
                String interval = getResources().getStringArray(R.array.rentas_interval_values)[positionSelected];
                String type = getResources().getStringArray(R.array.rentas_type_values)[positionSelectedType];
                if (OgspyActivity.activity.downloadRentasTask == null || OgspyActivity.activity.downloadRentasTask.canExecute()) {
                    DownloadRentabilitesTask rentasTask = new DownloadRentabilitesTask(OgspyActivity.activity, interval, type);
                    rentasTask.execute(new String[]{"do"});
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        return layout;
    }

    public static LinearLayout getPieChartContainer() {
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
}
