package com.ogsteam.ogspy.dialogs;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.utils.helpers.Pair;

/**
 * Created by Breizh on 15/10/2014.
 */
public class DialogHostiles extends DialogActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.details_hostiles);

        TextView title = (TextView) findViewById(R.id.details_hostiles_title);

        String ciblePlanet = b.getString("ciblePlanet");
        String cibleCoords = b.getString("cibleCoords");

        title.setText(ciblePlanet + " (" + cibleCoords + ")");

        String attacker = b.getString("attacker");
        int nbAttackers = 1;
        if (attacker.contains(SEPARATOR_HOSTILE_DETAIL)) {
            nbAttackers = attacker.split(SEPARATOR_HOSTILE_DETAIL).length;
        }

        if (nbAttackers > 1) {
            String[] attackers = b.getString("attacker").split(SEPARATOR_HOSTILE_DETAIL);
            String[] attackersPlanet = b.getString("attackerPlanet").split(SEPARATOR_HOSTILE_DETAIL);
            String[] attackersCoords = b.getString("attackerCoords").split(SEPARATOR_HOSTILE_DETAIL);
            String[] attackersCompo = b.getString("attackerCompo").split(SEPARATOR_HOSTILE_DETAIL);

            // Renseignement des valeurs
            for (int i = 0; i < 5; i++) {
                Pair idsHostile = getIdsHostiles(1 + i);

                String textAttacker = "";
                String textCompo = "";
                if (i < nbAttackers) {
                    textAttacker = attackers[i] + " - " + attackersPlanet[i] + " (" + attackersCoords[i] + ")";
                    textCompo = attackersCompo[i];
                }
                ((TextView) findViewById(idsHostile.keyInt)).setText(textAttacker);
                ((TextView) findViewById(idsHostile.valueInt)).setText(textCompo);
            }

        } else {
            String attackerPlanet = b.getString("attackerPlanet");
            String attackerCoords = b.getString("attackerCoords");
            String attackerCompo = b.getString("attackerCompo");

            ((TextView) findViewById(R.id.details_hotsiles_attaquant1)).setText(attacker + " - " + attackerPlanet + " (" + attackerCoords + ")");
            ((TextView) findViewById(R.id.details_hotsiles_attaquant1_compo)).setText(attackerCompo);

            for (int i = 1; i < 5; i++) {
                Pair idsHostile = getIdsHostiles(1 + i);

                ((TextView) findViewById(idsHostile.keyInt)).setText("");
                ((TextView) findViewById(idsHostile.valueInt)).setText("");
            }
        }
    }

    private Pair getIdsHostiles(int index) {
        Pair retour = new Pair(0, 0);
        if (index == 1) {
            retour = new Pair(R.id.details_hotsiles_attaquant1, R.id.details_hotsiles_attaquant1_compo);
        } else if (index == 2) {
            retour = new Pair(R.id.details_hotsiles_attaquant2, R.id.details_hotsiles_attaquant2_compo);
        } else if (index == 3) {
            retour = new Pair(R.id.details_hotsiles_attaquant3, R.id.details_hotsiles_attaquant3_compo);
        } else if (index == 4) {
            retour = new Pair(R.id.details_hotsiles_attaquant4, R.id.details_hotsiles_attaquant4_compo);
        } else if (index == 5) {
            retour = new Pair(R.id.details_hotsiles_attaquant5, R.id.details_hotsiles_attaquant5_compo);
        }
        return retour;
    }
}
