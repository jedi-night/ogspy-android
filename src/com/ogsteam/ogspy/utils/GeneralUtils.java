package com.ogsteam.ogspy.utils;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.fragments.tabs.GeneralFragment;
import com.ogsteam.ogspy.fragments.tabs.GeneralSpyItem;
import com.ogsteam.ogspy.fragments.tabs.GeneralSpyListAdapter;
import com.ogsteam.ogspy.helpers.AllianceHelper;
import com.ogsteam.ogspy.helpers.ServerHelper;
import com.ogsteam.ogspy.helpers.SpysHelper;

import java.util.ArrayList;

public class GeneralUtils {

	/** Display the general view */
	public static void showGeneral(ServerHelper serverHelper, AllianceHelper allianceHelper, SpysHelper spysHelper, OgspyActivity activity) {
        if(serverHelper != null && activity.getTitle() != null){
            activity.setTitle(serverHelper.getServerName());
        }
        if(allianceHelper != null && GeneralFragment.getAllianceOwn() != null){
            GeneralFragment.getAllianceOwn().setText(allianceHelper.getOwnAlliance());
            GeneralFragment.getNbMembers().setText(allianceHelper.getNbMembers());
        }
        if(spysHelper != null){
            ListView listPlayers = GeneralFragment.getMostCuriousPlayers();
            if(spysHelper.getMostCuriousPlayers().size() > 0){
                ArrayList<GeneralSpyItem> spysPlayer = new ArrayList<GeneralSpyItem>();
                for (Pair p : spysHelper.getMostCuriousPlayers()) {
                    spysPlayer.add(new GeneralSpyItem(p.key,p.value));
                }
                listPlayers.setAdapter(new GeneralSpyListAdapter(activity, spysPlayer));
            } else {
                ArrayList<String> noPlayerSpys = new ArrayList<String>();
                noPlayerSpys.add("Aucun espionnage");
                listPlayers.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, noPlayerSpys));
            }

            ListView listAlliances = GeneralFragment.getMostCuriousAlliances();
            if(spysHelper.getMostCuriousAlliances().size() > 0){
                ArrayList<GeneralSpyItem> spysAlly = new ArrayList<GeneralSpyItem>();
                for (Pair p : spysHelper.getMostCuriousAlliances()) {
                    spysAlly.add(new GeneralSpyItem(p.key, p.value));
                }
                listAlliances.setAdapter(new GeneralSpyListAdapter(activity, spysAlly));
            } else {
                ArrayList<String> noAllySpys = new ArrayList<String>();
                noAllySpys.add("Aucun espionnage");
                listAlliances.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, noAllySpys));
            }
        }
	}
}
