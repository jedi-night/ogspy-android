package com.ogsteam.ogspy.preferences;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Prefs;
import com.ogsteam.ogspy.utils.OgspyUtils;

public class Preferences {

	/** Display the account windows */
	public static void showPrefs(OgspyActivity activity) {		
		activity.setContentView(R.layout.prefs);
		Spinner spinner = (Spinner) activity.findViewById(R.id.prefs_timer_hostiles);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.prefs_timer_hostiles, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		if(!activity.getHandlerPrefs().getAllPrefs().isEmpty()){
			Prefs prefs = activity.getHandlerPrefs().getPrefsById(0); 
			if(prefs != null){
				if(prefs.getRefreshHostiles() > 0){
					int value = prefs.getRefreshHostiles();
					spinner.setSelection(value);
				}
			}
		}
	}
	
	/** Called when the user clicks the Save button in account*/
	public static void savePrefs(View view, OgspyActivity activity) {
		int selectedId = ((Spinner) activity.findViewById(R.id.prefs_timer_hostiles)).getSelectedItemPosition();

		if(activity.getHandlerPrefs().getPrefsCount() > 0){
			activity.getHandlerPrefs().deleteAllPrefs();
		}
		if(activity.getHandlerPrefs().addPrefs(new Prefs(0, selectedId)) != -1){
			Toast.makeText(activity, activity.getString(R.string.save_prefs_ok), Toast.LENGTH_LONG).show();
			// Affectation du timer
			OgspyActivity.setTimer(OgspyUtils.getTimerHostiles(activity, activity.getHandlerPrefs()));
			// Changement du check hostiles suivant le timer
			if(activity.autoUpdateHostiles != null){
				activity.autoUpdateHostiles.cancel();
				activity.autoUpdateHostiles.purge();
				activity.setAutomaticCheckHostiles();
			}
		} else {
			Toast.makeText(activity, activity.getString(R.string.save_prefs_ko), Toast.LENGTH_LONG).show();
		}
	}
}
