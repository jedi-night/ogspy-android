package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
        String linkBugsText = "<a href='https://github.com/jedi-night/ogspy-android/issues'>https://github.com</a>";
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
        String emailText = "<a href=\"https://github.com/jedi-night\">Jedinight</a>";
        email.setText(Html.fromHtml(emailText));
        email.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
