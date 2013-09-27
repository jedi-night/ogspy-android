package com.ogsteam.ogspy.fragments.tabs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.ui.charting.PieChart;


/**
 * @author mwho
 *
 */
public class RentabilitesFragment extends Fragment {
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

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        pie.addItem("Skadi", 23000000, Color.GREEN);
        pie.addItem("Odinn", 20000000, Color.YELLOW);
        pie.addItem("Prophet", 18000000, Color.WHITE);
        pie.addItem("brendan7", 17000000, Color.BLUE);
        pie.addItem("Windu", 15000000, Color.MAGENTA);
        pie.addItem("Penegal", 11000000, Color.RED);

        pie.setCurrentItem(0);
        pie.setShowText(true);
    }

    public static PieChart getPie() {
        return pie;
    }
}
