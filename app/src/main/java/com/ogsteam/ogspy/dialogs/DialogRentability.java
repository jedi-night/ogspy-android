package com.ogsteam.ogspy.dialogs;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.StringUtils;

/**
 * Created by Breizh on 15/10/2014.
 */
public class DialogRentability extends DialogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.details_rentability);

        String name = b.getString("name");
        int metal = b.getInt("metal");
        int cristal = b.getInt("cristal");
        int deuterium = b.getInt("deuterium");

        TextView title = (TextView) findViewById(R.id.details_rentability_title);
        title.setText(StringUtils.formatPattern(title.getText().toString(), name));

        TextView metalView = (TextView) findViewById(R.id.details_rentability_value_metal);
        TextView cristalView = (TextView) findViewById(R.id.details_rentability_value_cristal);
        TextView deuteriumView = (TextView) findViewById(R.id.details_rentability_value_deuterium);

        metalView.setText(NumberUtils.format(metal));
        cristalView.setText(NumberUtils.format(cristal));
        deuteriumView.setText(NumberUtils.format(deuterium));
    }
}
