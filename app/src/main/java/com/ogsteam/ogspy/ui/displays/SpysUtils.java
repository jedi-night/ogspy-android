package com.ogsteam.ogspy.ui.displays;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.dialogs.DialogHighscores;
import com.ogsteam.ogspy.fragments.tabs.SpysFragment;
import com.ogsteam.ogspy.fragments.tabs.items.GeneralSpyItem;
import com.ogsteam.ogspy.fragments.tabs.items.GeneralSpyListAdapter;
import com.ogsteam.ogspy.utils.helpers.AllianceHelper;
import com.ogsteam.ogspy.utils.helpers.Pair;
import com.ogsteam.ogspy.utils.helpers.ServerHelper;
import com.ogsteam.ogspy.utils.helpers.SpysHelper;
import com.ogsteam.ogspy.utils.helpers.Triplet;

import java.util.ArrayList;

public class SpysUtils {

    /**
     * Display the spys view
     */
    public static void showSpys(ServerHelper serverHelper, AllianceHelper allianceHelper, SpysHelper spysHelper, final OgspyActivity activity) {
        if (serverHelper != null && activity.getTitle() != null) {
            activity.mServerName = serverHelper.getServerName();
        }
        /*if(allianceHelper != null && SpysFragment.getAllianceOwn() != null){
            SpysFragment.getAllianceOwn().setText(allianceHelper.getOwnAlliance());
            SpysFragment.getNbMembers().setText(allianceHelper.getNbMembers());
        }*/
        if (spysHelper != null) {
            ListView listPlayers = SpysFragment.getMostCuriousPlayers();

            if (spysHelper.getMostCuriousPlayers().size() > 0) {
                ArrayList<GeneralSpyItem> spysPlayer = new ArrayList<GeneralSpyItem>();
                for (Triplet t : spysHelper.getMostCuriousPlayers()) {
                    spysPlayer.add(new GeneralSpyItem(t.keyLibelle, t.value, spysHelper.getHighscoreFromPlayername(t.key), t.key));
                }
                final GeneralSpyListAdapter adapterPlayer = new GeneralSpyListAdapter(activity, spysPlayer);
                listPlayers.setAdapter(adapterPlayer);
                listPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                        GeneralSpyItem item = (GeneralSpyItem) adapterPlayer.getItem(position);
                        Intent dialog = new Intent(activity, DialogHighscores.class);
                        dialog.putExtra("name", item.getName());
                        dialog.putExtra("type", DialogActivity.TYPE_HIGHSCORE_PLAYER);
                        dialog.putExtra("highscore", item.getHighscore());
                        activity.startActivity(dialog);
                    }
                });
                /*listPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        GeneralSpyItem item = adapterPlayer.getSelectedItem();
                        Log.d(this.getClass().getSimpleName(), "Spy selected : Player=" + item.getName() +" | nombre="+item.getNumber());
                    }
                });*/
            } else {
                ArrayList<String> noPlayerSpys = new ArrayList<String>();
                noPlayerSpys.add("Aucun espionnage");
                listPlayers.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, noPlayerSpys));
            }

            ListView listAlliances = SpysFragment.getMostCuriousAlliances();
            if (spysHelper.getMostCuriousAlliances().size() > 0) {
                ArrayList<GeneralSpyItem> spysAlly = new ArrayList<GeneralSpyItem>();
                for (Pair p : spysHelper.getMostCuriousAlliances()) {
                    spysAlly.add(new GeneralSpyItem(p.key, p.value, spysHelper.getHighscoreFromAllyname(p.key), p.key));
                }
                final GeneralSpyListAdapter adapterAlly = new GeneralSpyListAdapter(activity, spysAlly);
                listAlliances.setAdapter(adapterAlly);
                listAlliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                        GeneralSpyItem item = (GeneralSpyItem) adapterAlly.getItem(position);
                        Intent dialog = new Intent(activity, DialogHighscores.class);
                        dialog.putExtra("name", item.getShortName());
                        dialog.putExtra("type", DialogActivity.TYPE_HIGHSCORE_ALLY);
                        dialog.putExtra("highscore", item.getHighscore());
                        activity.startActivity(dialog);
                    }
                });
            } else {
                ArrayList<String> noAllySpys = new ArrayList<String>();
                noAllySpys.add("Aucun espionnage");
                listAlliances.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, noAllySpys));
            }
        }
    }
}
