package com.ogsteam.ogspy.utils;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.fragments.tabs.GeneralFragment;
import com.ogsteam.ogspy.helpers.AllianceHelper;
import com.ogsteam.ogspy.helpers.ServerHelper;
import com.ogsteam.ogspy.helpers.SpysHelper;

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
            listPlayers.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, spysHelper.getMostCuriousPlayers()));

            ListView listAlliances = GeneralFragment.getMostCuriousAlliances();
            listAlliances.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, spysHelper.getMostCuriousAlliances()));
        }
	}
}
