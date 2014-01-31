package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.helpers.Constants;
import com.ogsteam.ogspy.utils.objects.HighScore;

public class DialogActivity extends Activity {
    public static final int TYPE_HIGHSCORE_PLAYER = 1;
    public static final int TYPE_HIGHSCORE_ALLY = 2;
    public static final int TYPE_RENTA_DETAIL = 3;
    public static final int TYPE_NEW_ACCOUNT = TYPE_RENTA_DETAIL + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Bundle b = getIntent().getExtras();
        int type = b.getInt("type");
        if (TYPE_HIGHSCORE_PLAYER == type || TYPE_HIGHSCORE_ALLY == type) {
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


            generalPoints.setText(highscore.getPtsGeneral() > 0 ? NumberUtils.format(highscore.getPtsGeneral()) : "  -  ");
            militaryPoints.setText(highscore.getPtsMilitary() > 0 ? NumberUtils.format(highscore.getPtsMilitary()) : "  -  ");
            researchPoints.setText(highscore.getPtsResearch() > 0 ? NumberUtils.format(highscore.getPtsResearch()) : "  -  ");
            economicPoints.setText(highscore.getPtsEconomic() > 0 ? NumberUtils.format(highscore.getPtsEconomic()) : "  -  ");

            generalRank.setText(highscore.getPtsGeneral() > 0 ? highscore.getRankGeneral() : "  -  ");
            militaryRank.setText(highscore.getPtsMilitary() > 0 ? highscore.getRankMilitary() : "  -  ");
            researchRank.setText(highscore.getPtsResearch() > 0 ? highscore.getRankResearch() : "  -  ");
            economicRank.setText(highscore.getPtsEconomic() > 0 ? highscore.getRankEconomic() : "  -  ");

        } else if (TYPE_RENTA_DETAIL == type) {
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
        } else if (TYPE_NEW_ACCOUNT == type) {
            setContentView(R.layout.new_account);
            final EditText editTextUser = (EditText) findViewById(R.id.newAccountUserName);

            Spinner accountsList = (Spinner) findViewById(R.id.newAccountServers);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(OgspyPreferencesActivity.activity, R.array.servers, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            if (accountsList != null && adapter != null) {
                accountsList.setAdapter(adapter);
            }

            Button buttonSave = (Button) findViewById(R.id.buttonNewAccountSave);
            Button buttonCancel = (Button) findViewById(R.id.buttonNewAccountCancel);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextUser.getText().length() > 0) {
                        CommonUtilities.displayMessage(OgspyPreferencesActivity.activity, "Création du nouveau compte : " + editTextUser.getText());
                        finish();
                    } else {
                        CommonUtilities.displayMessage(OgspyPreferencesActivity.activity, "Problème lors de la saisie du compte !");
                    }
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private static boolean check(String username, String password, String serverUrl) throws OgspyException {
        boolean status = true;
        StringBuilder sb = new StringBuilder();

        if (username == null || username.length() == 0) {
            status = false;
            sb.append("Le nom d'utilisateur est obligatoire !");
        }
        if (password == null || password.length() == 0) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append("Le mot de passe est obligatoire !");
        }
        if (serverUrl == null || !serverUrl.matches("http://.*")) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append("L'adresse du serveur OGSPY est obligatoire et doit être cohérente (http://....)!");
        }
        if (sb.toString().length() > 0) {
            throw new OgspyException(sb.toString(), Constants.EXCEPTION_SAISIE);
        }
        return status;
    }
}
