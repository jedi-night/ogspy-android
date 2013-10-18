package com.ogsteam.ogspy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by jp.tessier on 16/07/13.
 */
public class OgspyAboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("A propos");

        setContentView(R.layout.about);

        // 1) How to replace link by text like "Click Here to visit Google" and
        // the text is linked with the website url ?
        TextView link = (TextView) findViewById(R.id.forumLink);
        String linkText = "<a href='http://forum.ogsteam.fr/index.php'>http://forum.ogsteam.fr</a>";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkBugs = (TextView) findViewById(R.id.bugsLink);
        String linkBugsText = "<a href='https://bitbucket.org/Jedinight/ogspy-android/issues?status=new&status=open'>https://bitbucket.org</a>";
        linkBugs.setText(Html.fromHtml(linkBugsText));
        linkBugs.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkPlayStore = (TextView) findViewById(R.id.playstoreLink);
        String linkPlayStoreText = "<a href='https://play.google.com/store/apps/details?id=com.ogsteam.ogspy&hl=fr'>https://play.google.com</a>";
        linkPlayStore.setText(Html.fromHtml(linkPlayStoreText));
        linkPlayStore.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkHelpInLine = (TextView) findViewById(R.id.helpLink);
        String linkHelpInLineText = "<a href='http://wiki.ogsteam.fr/doku.php?id=ogspy:android'>http://wiki.ogsteam.fr</a>";
        linkHelpInLine.setText(Html.fromHtml(linkHelpInLineText));
        linkHelpInLine.setMovementMethod(LinkMovementMethod.getInstance());

        // 2) How to place email address
        TextView email = (TextView) findViewById(R.id.developerName);
        String emailText = "Jedinight (<a href=\"mailto:jp.tessier.dev@gmail.com\">Jean-Philippe TESSIER</a>)";
        email.setText(Html.fromHtml(emailText));
        email.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        /*case R.id.ogspy_activity:
			//setContentView(R.layout.hostiles);
			// Step 1: Inflate layout
	        setContentView(R.layout.ogspy_tab_host);
	        mTabHost.setCurrentTab(0); //set the tab as per the saved state
			return true;*/
            case R.id.about:
                startActivity(new Intent(this, OgspyAboutActivity.class));
                return true;
            case R.id.prefs:
                //startActivityForResult(new Intent(this, OgspyPreferencesActivity.class), CODE_RETOUR_PREFS);
                startActivity(new Intent(this, OgspyPreferencesActivity.class));
                return true;
            case R.id.quit:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
