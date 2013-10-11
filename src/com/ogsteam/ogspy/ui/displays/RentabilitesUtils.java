package com.ogsteam.ogspy.ui.displays;

import android.graphics.Color;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.ui.charting.PieChart;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.helpers.RentabilitesHelper;

/**
 * Created by jp.tessier on 20/06/13.
 */
public abstract class RentabilitesUtils {

    public static void showRentabilites(RentabilitesHelper helperRentabilites, final OgspyActivity activity){
        PieChart pie = activity.getFragmentRentabilites().getPie();
        pie.removeAllItems();
        pie.refreshDrawableState();

        if(helperRentabilites!=null && helperRentabilites.getRentabilites() != null &&
                helperRentabilites.getRentabilites().size() > 0 && helperRentabilites.getRentabilites().containsKey(activity.getHandlerAccount().getAccountById(0).getUsername())){
            RentabilitesHelper.Rentabilite renta = helperRentabilites.getRentabilites().get(activity.getHandlerAccount().getAccountById(0).getUsername());
            pie.addItem("Métal", Float.parseFloat(renta.getMetal()), activity.getResources().getColor(R.color.pie_color_1));
            pie.addItem("Cristal", Float.parseFloat(renta.getCristal()), activity.getResources().getColor(R.color.pie_color_2));
            pie.addItem("Deutérium", Float.parseFloat(renta.getDeuterium()), activity.getResources().getColor(R.color.pie_color_3));
            activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
        } else {
            pie.addItem("Aucun",0,Color.BLACK);
            activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
        }
        pie.setCurrentItem(0);
        pie.setShowText(true);
        pie.refreshDrawableState();
    }
}
