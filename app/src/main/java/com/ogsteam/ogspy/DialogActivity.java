package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class DialogActivity extends Activity {
    public static DialogActivity activity;
    private static boolean isWaiting = false;
    public static int type = -1;
    public final Bundle b = new Bundle();

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
        b.clear();
        b.putAll(getIntent().getExtras());

        type = b.getInt("type");
        activity = this;
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
}
