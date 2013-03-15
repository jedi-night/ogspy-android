package com.ogsteam.ogspy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class OgspyActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
	        case R.id.settings:
	            showSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/** Display the settings windows */
	public void showSettings() {
		setContentView(R.layout.settings);
	}
	
	/** Called when the user clicks the Save button in Settings*/
	public void saveSettings(View view) {
		String userOgspy = ((EditText) findViewById(R.id.ogspy_user)).getText().toString();
		String password = ((EditText) findViewById(R.id.ogspy_password)).getText().toString();
		String serverUrl = ((EditText) findViewById(R.id.ogspy_server_url)).getText().toString();
		
	}
}
