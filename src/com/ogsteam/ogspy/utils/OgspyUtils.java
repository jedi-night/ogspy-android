package com.ogsteam.ogspy.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.DatabasePreferencesHandler;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.utils.security.MD5;
import com.ogsteam.ogspy.utils.security.SHA1;

public class OgspyUtils {

	public static String enryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return MD5.toMD5(SHA1.toSHA1(password));
	}
	
	public static String traiterReponseHostiles(String data, OgspyActivity activity){
		// ({"new_messages": 0,"type": "checkhostiles","check": 0,"user": "","execution": 14.19})
		NotificationProvider notifProvider = activity.getNotifProvider();
		String donnees = data.trim().substring(2, (data.trim().length()-3)); // suppress ({ and })
		String[] strArray = donnees.split(",");
		HashMap<String, ArrayList<String>> hashResponse = new HashMap<String, ArrayList<String>>();
		
		for(int i = 0; i < strArray.length; i++){
			String[] tabData = strArray[i].split(":");
			
			String value = tabData[1].replaceAll("\"","").trim();
			ArrayList<String> arrayValue = new ArrayList<String>();
			if(value.contains(";")){
				String[] tabValues = value.split(";");
				for(int j = 0; j < tabValues.length; j++){
					arrayValue.add(tabValues[j]);	
				}
			} else {
				arrayValue.add(value);
			}
						
			hashResponse.put(tabData[0].replaceAll("\"",""), arrayValue);
		}
		if(hashResponse.get("check").get(0).equals("1")){
			ArrayList<String> cibles = hashResponse.get("user");
			StringBuilder retour = new StringBuilder(activity.getString(R.string.hostiles_attack));
			StringBuilder notifHostile = new StringBuilder("");
			int cibleCount=0;
			for(String cible:cibles){
				cibleCount++;
				retour.append("\n\t- ").append(cible);
				notifHostile.append(cible);
				if(cibleCount < cibles.size()){
					notifHostile.append(", ");
				}
			}
			if(!NotificationProvider.notifHostilesAlreadyDone){
				NotificationProvider.notifHostilesAlreadyDone = true;
				notifProvider.createNotificationHostile(notifHostile.toString());
			}
			return retour.toString();
		} else {
			NotificationProvider.notifHostilesAlreadyDone = false;
			notifProvider.deleteNotificationHostile();
			return activity.getString(R.string.hostiles_none);
		}
		//return "Aucune information n'a pu être récupérée";
	}
	
	public static int getTimerHostiles(Activity activity, DatabasePreferencesHandler handlerPrefs){
		int timer = (Constants.TIMER_DEFAULT_VALUE * 60 * 1000);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.prefs_timer_hostiles, android.R.layout.simple_spinner_item);
		if(handlerPrefs.getPrefsCount() > 0){			
			int timeSet;
			try {
				timeSet = Integer.parseInt(adapter.getItem(handlerPrefs.getPrefsById(0).getRefreshHostiles()).toString());
			} catch (Exception e){
				timeSet = 0;
			}
			timer = (timeSet * 60 * 1000);
		}
		return timer;
	}
}
