package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jp.tessier on 16/07/13.
 */
public class OgspyWaitingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waiting);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
