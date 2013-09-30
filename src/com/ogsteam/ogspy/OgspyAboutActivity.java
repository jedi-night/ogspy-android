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
        String linkText = "<a href='http://forum.ogsteam.fr/index.php'>http://forum.ogsteam.fr/index.php</a>";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());

        // 2) How to place email address
        //TextView email = (TextView) findViewById(R.id.textView2);
        //String emailText = "Send email: <a href=\"mailto:person@stackoverflow.com\">Click Me!</a>";
        //email.setText(Html.fromHtml(emailText));
        //email.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}