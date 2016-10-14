package com.ogsteam.ogspy.preferences;

import android.content.SharedPreferences;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.OgspyException;
import com.ogsteam.ogspy.utils.helpers.Constants;

public class Preferences {

	/** Called when the user clicks the Save button in account*/
	public static void savePrefs() throws OgspyException {
        try{
            // Changement du check hostiles suivant le timer
            if(OgspyActivity.autoUpdateHostiles != null){
                OgspyActivity.autoUpdateHostiles.cancel();
                OgspyActivity.autoUpdateHostiles.purge();
            }
        }
        catch (Exception e) {
          throw  new OgspyException("Les réglages n'ont pu être sauvegardés", Constants.EXCEPTION_DATA_SAVE);
        }
	}
}
