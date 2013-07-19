package com.ogsteam.ogspy.preferences;

import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Prefs;
import com.ogsteam.ogspy.fragments.tabs.PrefsFragment;
import com.ogsteam.ogspy.utils.Constants;

public class Preferences {

	/** Display the account windows */
	public static void showPrefs(OgspyActivity activity, PrefsFragment fragment) {
		//activity.setContentView(R.layout.prefs);
		Spinner spinner = fragment.getTimerHostiles();
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.prefs_timer_hostiles, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		if(spinner != null && adapter != null) {
            spinner.setAdapter(adapter);
        }
		
		if(!activity.getHandlerPrefs().getAllPrefs().isEmpty()){
			Prefs prefs = activity.getHandlerPrefs().getPrefsById(0);
			if(prefs != null && prefs.getRefreshHostiles() > 0){
                int value = prefs.getRefreshHostiles();
                if(spinner != null) {
                    spinner.setSelection(value);
                }
			}
		}
	}
	
	/** Called when the user clicks the Save button in account*/
	public static void savePrefs(OgspyActivity activity, SharedPreferences preferences) throws OgspyException {
		//int selectedId = PrefsFragment.getTimerHostiles().getSelectedItemPosition();
        try{
            int timer = Integer.parseInt(preferences.getString("timer_hostiles", "0"));

            /*if(activity.getHandlerPrefs().getPrefsCount() > 0){
                activity.getHandlerPrefs().deleteAllPrefs();
            }
            if(activity.getHandlerPrefs().addPrefs(new Prefs(0, selectedId)) != -1){
                Toast.makeText(activity, activity.getString(R.string.save_prefs_ok), Toast.LENGTH_LONG).show();*/
            // Affectation du timer
            //OgspyActivity.setTimer(OgspyUtils.getTimerHostiles(activity, activity.getHandlerPrefs()));

            OgspyActivity.setTimer(timer);

            // Changement du check hostiles suivant le timer
            if(activity.autoUpdateHostiles != null){
                activity.autoUpdateHostiles.cancel();
                activity.autoUpdateHostiles.purge();
                activity.setAutomaticCheckHostiles();
            }
            /*} else {
                Toast.makeText(activity, activity.getString(R.string.save_prefs_ko), Toast.LENGTH_LONG).show();
            }*/
        } catch (Exception e) {
          throw  new OgspyException("Les réglages n'ont pu être sauvegardés", Constants.EXCEPTION_DATA_SAVE);
        }
	}
}
