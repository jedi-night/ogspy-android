package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.network.download.DownloadServerConnection;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.helpers.Pair;
import com.ogsteam.ogspy.utils.objects.HighScore;

public class DialogActivity extends Activity {
    public static DialogActivity activity;
    private static boolean isWaiting = false;

    public static final int TYPE_HIGHSCORE_PLAYER = 1;
    public static final int TYPE_HIGHSCORE_ALLY = 2;
    public static final int TYPE_RENTA_DETAIL = 3;
    public static final int TYPE_ACCOUNT = 4;
    public static final int TYPE_HOSTILE_DETAIL = 5;

    public static final String ACCOUNT_NEW = "NEW";
    public static final String ACCOUNT_MODIFY = "MODIFY";

    public static final String SEPARATOR_HOSTILE_DETAIL = ";";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        final Bundle b = getIntent().getExtras();
        int type = b.getInt("type");
        if (TYPE_HIGHSCORE_PLAYER == type || TYPE_HIGHSCORE_ALLY == type) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.details_highscore);
            HighScore highscore = (HighScore) b.get("highscore");
            String name = b.getString("name");
            TextView title = (TextView) findViewById(R.id.destails_highscore_title);
            if (TYPE_HIGHSCORE_ALLY == type) {
                ImageView imageTitle = (ImageView) findViewById(R.id.destails_highscore_image);
                imageTitle.setImageDrawable(OgspyActivity.activity.getResources().getDrawable(R.drawable.destails_highscore_alliance));

                title.setText(StringUtils.formatPattern(title.getText().toString(), name));
            } else if (TYPE_HIGHSCORE_PLAYER == type) {
                title.setText(StringUtils.formatPattern(title.getText().toString(), name));
            }

            TextView generalPoints = (TextView) findViewById(R.id.destails_highscore_points_points);
            TextView militaryPoints = (TextView) findViewById(R.id.destails_highscore_fleet_points);
            TextView researchPoints = (TextView) findViewById(R.id.destails_highscore_research_points);
            TextView economicPoints = (TextView) findViewById(R.id.destails_highscore_economic_points);

            TextView generalRank = (TextView) findViewById(R.id.destails_highscore_points_highscore);
            TextView militaryRank = (TextView) findViewById(R.id.destails_highscore_fleet_highscore);
            TextView researchRank = (TextView) findViewById(R.id.destails_highscore_research_highscore);
            TextView economicRank = (TextView) findViewById(R.id.destails_highscore_economic_highscore);


            generalPoints.setText(highscore != null && highscore.getPtsGeneral() > 0 ? NumberUtils.format(highscore.getPtsGeneral()) : "  -  ");
            militaryPoints.setText(highscore != null && highscore.getPtsMilitary() > 0 ? NumberUtils.format(highscore.getPtsMilitary()) : "  -  ");
            researchPoints.setText(highscore != null && highscore.getPtsResearch() > 0 ? NumberUtils.format(highscore.getPtsResearch()) : "  -  ");
            economicPoints.setText(highscore != null && highscore.getPtsEconomic() > 0 ? NumberUtils.format(highscore.getPtsEconomic()) : "  -  ");

            generalRank.setText(highscore != null && highscore.getPtsGeneral() > 0 ? highscore.getRankGeneral() : "  -  ");
            militaryRank.setText(highscore != null && highscore.getPtsMilitary() > 0 ? highscore.getRankMilitary() : "  -  ");
            researchRank.setText(highscore != null && highscore.getPtsResearch() > 0 ? highscore.getRankResearch() : "  -  ");
            economicRank.setText(highscore != null && highscore.getPtsEconomic() > 0 ? highscore.getRankEconomic() : "  -  ");

        } else if (TYPE_RENTA_DETAIL == type) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.details_rentability);

            String name = b.getString("name");
            int metal = b.getInt("metal");
            int cristal = b.getInt("cristal");
            int deuterium = b.getInt("deuterium");

            TextView title = (TextView) findViewById(R.id.details_rentability_title);
            title.setText(StringUtils.formatPattern(title.getText().toString(), name));

            TextView metalView = (TextView) findViewById(R.id.details_rentability_value_metal);
            TextView cristalView = (TextView) findViewById(R.id.details_rentability_value_cristal);
            TextView deuteriumView = (TextView) findViewById(R.id.details_rentability_value_deuterium);

            metalView.setText(NumberUtils.format(metal));
            cristalView.setText(NumberUtils.format(cristal));
            deuteriumView.setText(NumberUtils.format(deuterium));
        } else if (TYPE_ACCOUNT == type) {
            setContentView(R.layout.new_account);
            this.setFinishOnTouchOutside(false);
            showWaiting(false);

            final DatabaseAccountHandler accountHandler = OgspyActivity.activity.getHandlerAccount();
            final String creation = b.getString("creation");
            final String accountId = b.getString("accountId");

            final EditText user = (EditText) findViewById(R.id.newAccountUserName);
            final EditText password = (EditText) findViewById(R.id.newAccountPassword);
            final EditText serverOgspy = (EditText) findViewById(R.id.newAccountServerOgspy);

            final Spinner serversUniversList = (Spinner) findViewById(R.id.newAccountServers);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(OgspyPreferencesActivity.activity, R.array.servers, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            if (serversUniversList != null && adapter != null) {
                serversUniversList.setAdapter(adapter);
            }


            Button buttonDelete = (Button) findViewById(R.id.buttonNewAccountDelete);
            if (ACCOUNT_NEW.equals(creation)) {
                setTitle("Nouveau compte");
                buttonDelete.setVisibility(Button.INVISIBLE);
            } else {
                Account account = accountHandler.getAccountById(Integer.parseInt(accountId));
                setTitle(account.getUsername() + " - " + OgspyUtils.getUniversNameFromUrl(account.getServerUnivers()));

                user.setText(account.getUsername());
                password.setText(account.getPassword());
                serverOgspy.setText(account.getServerUrl());
                serversUniversList.setSelection(OgspyUtils.getUniversPositionFromUrl(account.getServerUnivers()));

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogHandler.confirm(DialogActivity.activity, "Suppression de compte", "Voulez-vous réellement supprimer le compte ?", "Annuler", "Ok",
                                new Runnable() {
                                    public void run() {
                                        deleteAccount(accountHandler, accountId);
                                    }
                                },
                                new Runnable() {
                                    public void run() {
                                    }
                                }
                        );

                    }
                });
            }
            Button buttonSave = (Button) findViewById(R.id.buttonNewAccountSave);
            Button buttonCancel = (Button) findViewById(R.id.buttonNewAccountCancel);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OgspyUtils.checkUserAccount(user.getText().toString(), password.getText().toString(), serverOgspy.getText().toString())) {
                        Account account;
                        if (ACCOUNT_NEW.equals(creation)) {
                            account = new Account(user.getText().toString(), password.getText().toString(), serverOgspy.getText().toString(), getServerUrlFromSelectedPosition(serversUniversList.getSelectedItemPosition()));
                        } else {
                            account = new Account(Integer.parseInt(accountId), user.getText().toString(), password.getText().toString(), serverOgspy.getText().toString(), getServerUrlFromSelectedPosition(serversUniversList.getSelectedItemPosition()));
                        }
                        DownloadServerConnection serverConnection = new DownloadServerConnection(OgspyActivity.activity, DialogActivity.activity, account, creation);
                        DownloadTask.executeDownload(OgspyActivity.activity, serverConnection);
                    }
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else if (TYPE_HOSTILE_DETAIL == type) {
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





            /*
            "arrivalTime"
            "ciblePlanet"
            "cibleCoords"
            "attacker"
            "attackerPlanet"
            "attackerCoords"
            */

        }
    }

    private void deleteAccount(DatabaseAccountHandler accountHandler, String accountId) {
        if (accountHandler.deleteAccountById(accountId) != -1) {
            CommonUtilities.displayMessage(OgspyActivity.activity, "Le compte a été supprimé");
            OgspyPreferencesActivity.activity.refreshAcountsList((ListPreference) OgspyPreferencesActivity.activity.findPreference("prefs_accounts"));
        } else {
            new DialogHandler().showException(OgspyActivity.activity, new OgspyException("Le compte n'a pu être supprimé", Constants.EXCEPTION_DATA_DELETE));
        }
        finish();
    }

    private String getServerUrlFromSelectedPosition(int positionSelected) {
        return OgspyActivity.activity.getResources().getStringArray(R.array.servers_values)[positionSelected];
    }

    public void showWaiting(boolean visible) {
        if (visible) {
            if (!isWaiting) {
                activity.findViewById(R.id.tabcontent).setVisibility(View.GONE);
                activity.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                isWaiting = true;
            }
        } else {
            activity.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            activity.findViewById(R.id.tabcontent).setVisibility(View.VISIBLE);
            isWaiting = false;
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
