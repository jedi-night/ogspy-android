package com.ogsteam.ogspy.ui.displays;

import android.graphics.Color;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.ui.charting.PieChart;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.RentabilitesHelper;

import java.util.ArrayList;

/**
 * Created by jp.tessier on 20/06/13.
 */
public abstract class RentabilitesUtils {

    public static void showRentabilites(RentabilitesHelper helperRentabilites, final OgspyActivity activity, String type) {
        PieChart pie = activity.getFragmentRentabilites().getPie();
        pie.removeAllItems();

        if (helperRentabilites != null && helperRentabilites.getRentabilites() != null && helperRentabilites.getRentabilites().size() > 0) {
            if (Constants.RENTA_TYPE_ME.equals(type) && helperRentabilites.getRentabilites().containsKey(activity.getHandlerAccount().getAccountById(0).getUsername())) {
                RentabilitesHelper.Rentabilite renta = helperRentabilites.getRentabilites().get(activity.getHandlerAccount().getAccountById(0).getUsername());
                pie.addItem("Métal", Float.parseFloat(renta.getMetal()), activity.getResources().getColor(R.color.pie_color_1));
                pie.addItem("Cristal", Float.parseFloat(renta.getCristal()), activity.getResources().getColor(R.color.pie_color_2));
                pie.addItem("Deutérium", Float.parseFloat(renta.getDeuterium()), activity.getResources().getColor(R.color.pie_color_3));
                activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
            } else if (Constants.RENTA_TYPE_ALLY.equals(type)) {
                RentabilitesHelper.Rentabilite renta = helperRentabilites.getTotalRentabilite();
                pie.addItem("Métal", Float.parseFloat(renta.getMetal()), activity.getResources().getColor(R.color.pie_color_1));
                pie.addItem("Cristal", Float.parseFloat(renta.getCristal()), activity.getResources().getColor(R.color.pie_color_2));
                pie.addItem("Deutérium", Float.parseFloat(renta.getDeuterium()), activity.getResources().getColor(R.color.pie_color_3));
                activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
            } else if (Constants.RENTA_TYPE_MEMBER.equals(type)) {
                ArrayList<RentabilitesHelper.Rentabilite> rentas = helperRentabilites.getRentabilitesSortedByGains();
                for (int i = 0; i < rentas.size(); i++) {
                    RentabilitesHelper.Rentabilite renta = rentas.get(i);
                    pie.addItem(renta.getUser(), Float.parseFloat(renta.getGains()), activity.getResources().getColor(getPieColor(i)));
                }
                activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
            } else {
                pie.addItem("Aucun", 0, Color.BLACK);
                activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
            }
        } else {
            pie.addItem("Aucun",0,Color.BLACK);
            activity.getFragmentRentabilites().getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(pie.getTotal()));
        }
        pie.setCurrentItem(0);
        pie.refreshDrawableState();
    }

    private static int getPieColor(int i) {
        if (i == 2) {
            return R.color.pie_color_2;
        } else if (i == 3) {
            return R.color.pie_color_3;
        } else if (i == 4) {
            return R.color.pie_color_4;
        } else if (i == 5) {
            return R.color.pie_color_5;
        } else if (i == 6) {
            return R.color.pie_color_6;
        } else if (i == 7) {
            return R.color.pie_color_7;
        } else if (i == 8) {
            return R.color.pie_color_8;
        } else if (i == 9) {
            return R.color.pie_color_9;
        } else if (i == 10) {
            return R.color.pie_color_10;
        }
        return R.color.pie_color_1;
    }
}
