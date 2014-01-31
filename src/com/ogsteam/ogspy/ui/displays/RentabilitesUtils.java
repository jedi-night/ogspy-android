package com.ogsteam.ogspy.ui.displays;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.fragments.tabs.RentabilitesFragment;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.RentabilitesHelper;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jp.tessier on 20/06/13.
 */
public abstract class RentabilitesUtils {

    public static void showRentabilites(RentabilitesHelper helperRentabilites, final OgspyActivity activity, String type) {
        final RentabilitesHelper helperRenta = helperRentabilites;
        CategorySeries categories = new CategorySeries("Rentabilité");
        if (helperRentabilites != null && helperRentabilites.getRentabilites() != null && helperRentabilites.getRentabilites().size() > 0) {
            if (Constants.RENTA_TYPE_ME.equals(type) && helperRentabilites.getRentabilites().containsKey(activity.getHandlerAccount().getAccountById(0).getUsername())) {
                RentabilitesHelper.Rentabilite renta = helperRentabilites.getRentabilites().get(activity.getHandlerAccount().getAccountById(0).getUsername());
                addCategories(type, categories, renta);
                setRentabiliteText(activity, Float.parseFloat(String.valueOf(categories.getValue(0) + categories.getValue(1) + categories.getValue(2))));
            } else if (Constants.RENTA_TYPE_ALLY.equals(type)) {
                RentabilitesHelper.Rentabilite renta = helperRentabilites.getTotalRentabilite();
                addCategories(type, categories, renta);
                setRentabiliteText(activity, Float.parseFloat(String.valueOf(categories.getValue(0) + categories.getValue(1) + categories.getValue(2))));
            } else if (Constants.RENTA_TYPE_MEMBER.equals(type)) {
                ArrayList<RentabilitesHelper.Rentabilite> rentas = helperRentabilites.getRentabilitesSortedByGains();
                float rentaGlobale = 0f;
                for (int i = 0; i < rentas.size(); i++) {
                    RentabilitesHelper.Rentabilite renta = rentas.get(i);
                    addCategories(type, categories, renta);
                    rentaGlobale += Float.parseFloat(renta.getGains());
                }
                setRentabiliteText(activity, rentaGlobale);
            } else {
                setRentabiliteText(activity, 0);
            }
        } else {
            setRentabiliteText(activity, 0);
        }
        if (categories.getItemCount() > 0) {
            GraphicalView piechart = ChartFactory.getPieChartView(activity, categories, getDefaultRenderer(categories.getItemCount()));
            if (Constants.RENTA_TYPE_MEMBER.equals(type)) {
                addPieOnClickListener(activity, helperRenta, piechart);
            }
            RelativeLayout piChartContainer = ((RentabilitesFragment) activity.getFragmentRentabilites()).getPieChartContainer();
            if (piChartContainer != null) {
                piChartContainer.removeAllViews();
                piChartContainer.addView(piechart, new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }

    private static void addCategories(String type, CategorySeries categories, RentabilitesHelper.Rentabilite renta) {
        if (Constants.RENTA_TYPE_ME.equals(type) || Constants.RENTA_TYPE_ALLY.equals(type)) {
            categories.add("Métal", Float.parseFloat(renta.getMetal()));
            categories.add("Cristal", Float.parseFloat(renta.getCristal()));
            categories.add("Deutérium", Float.parseFloat(renta.getDeuterium()));
        } else {
            categories.add(renta.getUser(), Float.parseFloat(renta.getGains()));
        }
    }

    private static void setRentabiliteText(OgspyActivity activity, float value) {
        RentabilitesFragment rentaFragment = ((RentabilitesFragment) activity.getFragmentRentabilites());
        if (rentaFragment != null && rentaFragment.getRentabilityTotal() != null) {
            ((RentabilitesFragment) activity.getFragmentRentabilites()).getRentabilityTotal().setText("Rentabilité : " + NumberUtils.format(value));
        }
    }

    private static void addPieOnClickListener(final OgspyActivity activity, final RentabilitesHelper helperRenta, GraphicalView piechart) {
        piechart.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = ((GraphicalView) v).getCurrentSeriesAndPoint();
                if (seriesSelection == null) {
                    CommonUtilities.displayMessage(activity, "No chart element was clicked");
                } else {
                    ArrayList<RentabilitesHelper.Rentabilite> rentas = helperRenta.getRentabilitesSortedByGains();
                    RentabilitesHelper.Rentabilite renta = rentas.get(seriesSelection.getPointIndex());
                    /*"Chart element in series index " + seriesSelection.getSeriesIndex() + " data point index " + seriesSelection.getPointIndex() + " was clicked" + " closest point value X=" + seriesSelection.getXValue() + ", Y=" + seriesSelection.getValue());*/
                    /*CommonUtilities.displayMessage(activity, "Détail de la rentabilité du joueur " + renta.getUser() + "\n\n" +
                            "Métal\t\t\t\t : " + NumberUtils.format(Float.parseFloat(renta.getMetal())) +
                            "\nCristal\t\t\t : " + NumberUtils.format(Float.parseFloat(renta.getCristal())) +
                            "\nDeutérium\t\t : " + NumberUtils.format(Float.parseFloat(renta.getDeuterium())));*/
                    Intent dialog = new Intent(activity, DialogActivity.class);
                    dialog.putExtra("name", renta.getUser());
                    dialog.putExtra("metal", renta.getMetalInt());
                    dialog.putExtra("cristal", renta.getCristalInt());
                    dialog.putExtra("deuterium", renta.getDeuteriumInt());
                    dialog.putExtra("type", DialogActivity.TYPE_RENTA_DETAIL);
                    activity.startActivity(dialog);
                }
            }
        });
    }

    private static DefaultRenderer getDefaultRenderer(int nbOfItems) {
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 1; i <= nbOfItems; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(getPieColor(i));
            seriesRenderer.setDisplayChartValues(true);
            seriesRenderer.setChartValuesFormat(new DecimalFormat("###,###"));
            seriesRenderer.setDisplayChartValuesDistance(0);
            //seriesRenderer.setShowLegendItem(true);
            seriesRenderer.setChartValuesTextSize(24);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setLabelsTextSize(18);
        defaultRenderer.setLabelsColor(Color.WHITE);
        defaultRenderer.setLegendTextSize(24);
        defaultRenderer.setDisplayValues(true);
        defaultRenderer.setBackgroundColor(DefaultRenderer.NO_COLOR);
        defaultRenderer.setZoomButtonsVisible(false);
        defaultRenderer.setClickEnabled(true);
        defaultRenderer.setPanEnabled(true);
        defaultRenderer.setAntialiasing(true);
        defaultRenderer.setShowLabels(true);
        //defaultRenderer.setShowLegend(true);
        return defaultRenderer;
    }

    private static int getPieColor(int i) {
        if (i == 2) {
            return Color.parseColor("#55BF3B");
        } else if (i == 3) {
            return Color.parseColor("#DF5353");
        } else if (i == 4) {
            return Color.parseColor("#7798BF");
        } else if (i == 5) {
            return Color.parseColor("#AAEEEE");
        } else if (i == 6) {
            return Color.parseColor("#FF0066");
        } else if (i == 7) {
            return Color.parseColor("#EEAAEE");
        } else if (i == 8) {
            return Color.parseColor("#55BF3B");
        } else if (i == 9) {
            return Color.parseColor("#DF5353");
        } else if (i == 10) {
            return Color.parseColor("#7798BF");
        }
        return Color.parseColor("#DDDF0D");
    }
}
