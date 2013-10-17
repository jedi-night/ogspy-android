package com.ogsteam.ogspy.fragments.tabs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.download.DownloadRentabilitesTask;
import com.ogsteam.ogspy.ui.charting.PieChart;


/**
 * @author mwho
 *
 */
public class RentabilitesFragment extends Fragment {
    private static Spinner rentabiliteInterval;
    private static TextView rentabilityTotal;
    private static PieChart pie;


    /** (non-Javadoc)
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

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.rentabilite, container, false);

        pie =  (PieChart) layout.findViewById(R.id.pie);
        pie.removeAllItems();
        pie.addItem("",0,Color.BLACK);

        rentabilityTotal = (TextView) layout.findViewById(R.id.rentabilityTotal);

        //activity.setContentView(R.layout.prefs);
        rentabiliteInterval = (Spinner) layout.findViewById(R.id.rentas_interval);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(OgspyActivity.activity, R.array.rentas_interval, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if(rentabiliteInterval != null && adapter != null) {
            rentabiliteInterval.setAdapter(adapter);
        }
        rentabiliteInterval.setSelection(0);

        rentabiliteInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positionSelected = getRentabiliteInterval().getSelectedItemPosition();
                String interval = getResources().getStringArray(R.array.rentas_interval_values)[positionSelected];
                DownloadRentabilitesTask rentasTask = new DownloadRentabilitesTask(OgspyActivity.activity, interval);
                rentasTask.execute(new String[]{"do"});
                //CommonUtilities.displayMessage(OgspyActivity.activity, "Sélection de l'intervalle de rentabilité =" + interval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static PieChart getPie() {
        return pie;
    }

    public static Spinner getRentabiliteInterval() {
        return rentabiliteInterval;
    }

    public static TextView getRentabilityTotal() {
        return rentabilityTotal;
    }
}
