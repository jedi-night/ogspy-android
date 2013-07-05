package com.ogsteam.ogspy.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.fragments.tabs.HostileFragment;
import com.ogsteam.ogspy.fragments.tabs.HostileItem;
import com.ogsteam.ogspy.fragments.tabs.HostilesListAdapter;
import com.ogsteam.ogspy.helpers.HostilesHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by jp.tessier on 20/06/13.
 */
public abstract class HostileUtils {

    public static void showHostiles(HostilesHelper helperHostile, final OgspyActivity activity){
        ArrayList<HostileItem> hostileItems = new ArrayList<HostileItem>();
        if(helperHostile != null && (helperHostile.getAttaques().size() > 0 || helperHostile.getAttaquesGroup().size() > 0)){
            HostileItem item = null;
            // Attaques simples
            for ( Iterator<String> user = helperHostile.getAttaques().keySet().iterator(); user.hasNext(); ) {
                String userAttack = user.next();
                for(HostilesHelper.Cible cible: helperHostile.getAttaques().get(userAttack)){
                    item = new HostileItem(false);
                    item.setImage(R.drawable.hostiles_simple_attack);
                    item.setTitle(userAttack, cible.getCiblePlanet(), cible.getCibleCoords());
                    item.setDate(cible.getArrivalTime());
                    item.setDetail(cible.getOriginPlanet(), cible.getOriginCoords(), cible.getAttacker(),false,null);
                    item.setCompo(cible.getCompo());
                    hostileItems.add(item);
                }
            }
            // Attaques groupées
            for ( Iterator<String> ag = helperHostile.getAttaquesGroup().keySet().iterator(); ag.hasNext(); ) {
                String idAttack = ag.next();
                item = new HostileItem(true);
                HostilesHelper.AG attaqueGroup = helperHostile.getAttaquesGroup().get(idAttack).get(idAttack);
                item.setImage(R.drawable.hostiles_group_attack);
                item.setTitle(attaqueGroup.getCible(), attaqueGroup.getCiblePlanet(), attaqueGroup.getCibleCoords());
                item.setDate(attaqueGroup.getArrivalTime());

                StringBuffer detail = new StringBuffer();
                TreeMap<String,HostilesHelper.Vague> vagues = attaqueGroup.getVagues();
                int coutnVague=1;
                for(Iterator<String> vague = vagues.keySet().iterator(); vague.hasNext();){
                    String idVague = vague.next();
                    HostilesHelper.Vague vagueItem = vagues.get(idVague);

                    detail.append(HostileItem.getDetail(vagueItem.getOriginPlanet(), vagueItem.getOriginCoords(), vagueItem.getAttacker(),true,vagueItem.getCompo()));
                    if(coutnVague < vagues.size()){
                        detail.append("\n");
                    }
                    coutnVague++;
                }
                item.setDetail(detail.toString());
                hostileItems.add(item);
            }
            final ListView lv1 = HostileFragment.getListHostiles();
            if(lv1!=null){
                lv1.setAdapter(new HostilesListAdapter(activity, hostileItems));
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        Object o = lv1.getItemAtPosition(position);
                        HostileItem hostileData = (HostileItem) o;
                        Toast.makeText(activity, hostileData.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
