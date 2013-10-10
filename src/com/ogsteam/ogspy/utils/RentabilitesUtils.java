package com.ogsteam.ogspy.utils;

import android.graphics.Color;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.helpers.RentabilitesHelper;
import com.ogsteam.ogspy.ui.charting.PieChart;

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
        } else {
            pie.addItem("Aucun",0,Color.BLACK);
        }
        pie.setCurrentItem(0);
        pie.setShowText(true);
        pie.refreshDrawableState();
    }
}
