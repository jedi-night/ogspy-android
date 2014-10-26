package com.ogsteam.ogspy.ui.displays;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
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

    public static void showHostiles(HostilesHelper helperHostile, final OgspyActivity activity) {
        ArrayList<HostileItem> hostileItems = new ArrayList<HostileItem>();
        final ListView hostilesListView = HostileFragment.getListHostiles();
        if (helperHostile != null && (helperHostile.getAttaques().size() > 0 || helperHostile.getAttaquesGroup().size() > 0)) {
            HostileItem item = null;
            // Attaques simples
            for (Iterator<String> user = helperHostile.getAttaques().keySet().iterator(); user.hasNext(); ) {
                String userAttack = user.next();
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
            for (Iterator<String> ag = helperHostile.getAttaquesGroup().keySet().iterator(); ag.hasNext(); ) {
                String idAttack = ag.next();
                item = new HostileItem(true);
                HostilesHelper.AG attaqueGroup = helperHostile.getAttaquesGroup().get(idAttack).get(idAttack);
                item.setImage(R.drawable.hostiles_group_attack);
                item.setTitle(attaqueGroup.getCible(), attaqueGroup.getCiblePlanet(), attaqueGroup.getCibleCoords());
                item.setDate(attaqueGroup.getArrivalTime());

                StringBuffer detail = new StringBuffer();
                TreeMap<String, HostilesHelper.Vague> vagues = attaqueGroup.getVagues();
                int coutnVague = 1;
                for (Iterator<String> vague = vagues.keySet().iterator(); vague.hasNext(); ) {
                    String idVague = vague.next();
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
                final HostilesListAdapter adapterHostile = new HostilesListAdapter(activity, hostileItems);
                hostilesListView.setAdapter(adapterHostile);
                hostilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        HostileItem item = (HostileItem) adapterHostile.getItem(position);
                        Intent dialog = new Intent(activity, DialogHostiles.class);
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
                            for (Iterator<String> it = item.getAg().getVagues().keySet().iterator(); it.hasNext(); ) {
                                compteurVagues++;

                                String key = it.next();
                                HostilesHelper.Vague vague = item.getAg().getVagues().get(key);

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
                        activity.startActivity(dialog);
                            /*Object o = hostilesListView.getItemAtPosition(position);
                            HostileItem hostileData = (HostileItem) o;
                            new DialogHandler().confirm(activity,
                                                        "Détail de la flotte", hostileData.toString(), null, "Ok", new Runnable() {
                                public void run() {
                                }
                            },null);*/
                        //Toast.makeText(activity, hostileData.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            if (hostilesListView != null) {
                ArrayList<String> noHostiles = new ArrayList<String>();
                noHostiles.add("Aucune flotte hostile en approche sur un des joueurs de la communauté");
                hostilesListView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, noHostiles));
            }
        }
    }
}
