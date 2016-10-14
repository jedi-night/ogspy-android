package com.ogsteam.ogspy.ui.displays;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyApplication;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.dialogs.DialogHostiles;
import com.ogsteam.ogspy.fragments.tabs.HostileFragment;
import com.ogsteam.ogspy.fragments.tabs.items.HostileItem;
import com.ogsteam.ogspy.fragments.tabs.items.HostilesListAdapter;
import com.ogsteam.ogspy.utils.helpers.HostilesHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by jp.tessier on 20/06/13.
 */
public abstract class HostileUtils {

    public static void showHostiles(HostilesHelper helperHostile) {
        ArrayList<HostileItem> hostileItems = new ArrayList<>();
        final ListView hostilesListView = HostileFragment.getListHostiles();
        if (helperHostile != null && (helperHostile.getAttaques().size() > 0 || helperHostile.getAttaquesGroup().size() > 0)) {
            HostileItem item = null;
            // Attaques simples
            for (String userAttack : helperHostile.getAttaques().keySet()) {
                for (HostilesHelper.Cible cible : helperHostile.getAttaques().get(userAttack)) {
                    item = new HostileItem(false);
                    item.setImage(R.drawable.hostiles_simple_attack);
                    item.setTitle(userAttack, cible.getCiblePlanet(), cible.getCibleCoords());
                    item.setDate(cible.getArrivalTime());
                    item.setDetail(cible.getOriginPlanet(), cible.getOriginCoords(), cible.getAttacker(), false, null);
                    item.setCompo(cible.getCompo());
                    item.setCible(cible);
                    hostileItems.add(item);
                }
            }
            // Attaques groupées
            for (String idAttack : helperHostile.getAttaquesGroup().keySet()) {
                item = new HostileItem(true);
                HostilesHelper.AG attaqueGroup = helperHostile.getAttaquesGroup().get(idAttack).get(idAttack);
                item.setImage(R.drawable.hostiles_group_attack);
                item.setTitle(attaqueGroup.getCible(), attaqueGroup.getCiblePlanet(), attaqueGroup.getCibleCoords());
                item.setDate(attaqueGroup.getArrivalTime());

                StringBuffer detail = new StringBuffer();
                TreeMap<String, HostilesHelper.Vague> vagues = attaqueGroup.getVagues();
                int coutnVague = 1;
                for (String idVague : vagues.keySet()) {
                    HostilesHelper.Vague vagueItem = vagues.get(idVague);

                    detail.append(HostileItem.getDetail(vagueItem.getOriginPlanet(), vagueItem.getOriginCoords(), vagueItem.getAttacker(), true, vagueItem.getCompo()));
                    if (coutnVague < vagues.size()) {
                        detail.append("\n");
                    }
                    coutnVague++;
                }
                item.setDetail(detail.toString());
                item.setAg(attaqueGroup);
                hostileItems.add(item);
            }
            if (hostilesListView != null) {
                final HostilesListAdapter adapterHostile = new HostilesListAdapter(OgspyApplication.getContext(), hostileItems);
                hostilesListView.setAdapter(adapterHostile);
                hostilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        HostileItem item = (HostileItem) adapterHostile.getItem(position);
                        Intent dialog = new Intent(OgspyApplication.getContext(), DialogHostiles.class);
                        if (item.getCible() != null) {
                            dialog.putExtra("arrivalTime", item.getCible().getArrivalTime());
                            dialog.putExtra("ciblePlanet", item.getCible().getCiblePlanet());
                            dialog.putExtra("cibleCoords", item.getCible().getCibleCoords());
                            dialog.putExtra("attacker", item.getCible().getAttacker());
                            dialog.putExtra("attackerPlanet", item.getCible().getOriginPlanet());
                            dialog.putExtra("attackerCoords", item.getCible().getOriginCoords());
                            dialog.putExtra("attackerCompo", item.getCible().getCompo());
                        } else {
                            dialog.putExtra("arrivalTime", item.getAg().getArrivalTime());
                            dialog.putExtra("ciblePlanet", item.getAg().getCiblePlanet());
                            dialog.putExtra("cibleCoords", item.getAg().getCibleCoords());

                            StringBuilder attacker = new StringBuilder("");
                            StringBuilder attackerPlanet = new StringBuilder("");
                            StringBuilder attackerCoords = new StringBuilder("");
                            StringBuilder attackerCompo = new StringBuilder("");
                            int compteurVagues = 0;
                            for (String s : item.getAg().getVagues().keySet()) {
                                compteurVagues++;

                                HostilesHelper.Vague vague = item.getAg().getVagues().get(s);

                                attacker.append(vague.getAttacker());
                                attackerPlanet.append(vague.getOriginPlanet());
                                attackerCoords.append(vague.getOriginCoords());
                                attackerCompo.append(vague.getCompo());
                                if (compteurVagues < item.getAg().getVagues().size()) {
                                    attacker.append(";");
                                    attackerPlanet.append(";");
                                    attackerCoords.append(";");
                                    attackerCompo.append(";");
                                }
                            }
                            dialog.putExtra("attacker", attacker.toString());
                            dialog.putExtra("attackerPlanet", attackerPlanet.toString());
                            dialog.putExtra("attackerCoords", attackerCoords.toString());
                            dialog.putExtra("attackerCompo", attackerCompo.toString());
                        }
                        dialog.putExtra("type", DialogActivity.TYPE_HOSTILE_DETAIL);
                        OgspyApplication.getApplication().startActivity(dialog);
                    }
                });
            }
        } else {
            if (hostilesListView != null) {
                ArrayList<String> noHostiles = new ArrayList<>();
                noHostiles.add("Aucune flotte hostile en approche sur un des joueurs de la communauté");
                hostilesListView.setAdapter(new ArrayAdapter<>(OgspyApplication.getContext(), android.R.layout.simple_list_item_1, noHostiles));
            }
        }
    }
}
