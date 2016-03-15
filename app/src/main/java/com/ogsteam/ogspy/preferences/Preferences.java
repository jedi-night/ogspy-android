package com.ogsteam.ogspy.preferences;

import android.content.SharedPreferences;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.utils.helpers.Constants;

public class Preferences {

	/** Called when the user clicks the Save button in account*/
	public static void savePrefs(OgspyActivity activity, SharedPreferences preferences) throws OgspyException {
		//int selectedId = PrefsFragment.getTimerHostiles().getSelectedItemPosition();
        try{
            //int timer = Integer.parseInt(preferences.getString("timer_hostiles", "0"));

            /*if(activity.getHandlerPrefs().getPrefsCount() > 0){
                activity.getHandlerPrefs().deleteAllPrefs();
            }
            if(activity.getHandlerPrefs().addPrefs(new Prefs(0, selectedId)) != -1){
                Toast.makeText(activity, activity.getString(R.string.save_prefs_ok), Toast.LENGTH_LONG).show();*/
            // Affectation du timer
            //OgspyActivity.setTimer(OgspyUtils.getTimerHostiles(activity, activity.getHandlerPrefs()));

            //OgspyActivity.setTimer(timer);

            // Changement du check hostiles suivant le timer
            if(activity.autoUpdateHostiles != null){
                activity.autoUpdateHostiles.cancel();
                activity.autoUpdateHostiles.purge();
                //activity.setAutomaticCheckHostiles();
            }
            /*} else {
                Toast.makeText(activity, activity.getString(R.string.save_prefs_ko), Toast.LENGTH_LONG).show();
            }*/
        } catch (Exception e) {
          throw  new OgspyException("Les réglages n'ont pu être sauvegardés", Constants.EXCEPTION_DATA_SAVE);
        }
	}
}
