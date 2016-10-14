package com.ogsteam.ogspy.dialogs;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogsteam.ogspy.DialogActivity;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.utils.NumberUtils;
import com.ogsteam.ogspy.utils.StringUtils;
import com.ogsteam.ogspy.utils.objects.HighScore;

/**
 * Created by Breizh on 15/10/2014.
 */
public class DialogHighscores extends DialogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.details_highscore);
        HighScore highscore = (HighScore) b.get("highscore");
        String name = b.getString("name");
        TextView title = (TextView) findViewById(R.id.destails_highscore_title);
        if (TYPE_HIGHSCORE_ALLY == type) {
            ImageView imageTitle = (ImageView) findViewById(R.id.destails_highscore_image);
            imageTitle.setImageDrawable(getResources().getDrawable(R.drawable.destails_highscore_alliance));

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
    }
}
