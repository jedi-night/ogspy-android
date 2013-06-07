package com.ogsteam.ogspy.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.DatabasePreferencesHandler;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.utils.security.MD5;
import com.ogsteam.ogspy.utils.security.SHA1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class OgspyUtils {

	public static String enryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return MD5.toMD5(SHA1.toSHA1(password));
	}
	
	public static String traiterReponseHostiles(JSONObject data, OgspyActivity activity){
		NotificationProvider notifProvider = activity.getNotifProvider();
		
		try {
			JSONObject json = data.getJSONObject("hostile");
			if(json.length() > 0 && json.getString("isAttack").equals("1")){
				JSONArray cibles = json.getJSONArray("user");
				
				StringBuilder retour = new StringBuilder(activity.getString(R.string.hostiles_attack));
				StringBuilder notifHostile = new StringBuilder("");
				for(int i=0;i < cibles.length(); i++){
					String cible = cibles.getString(i);
					retour.append("\n\t- ").append(cible);
					notifHostile.append(cible);
					if(i < cibles.length()){
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
		} catch (JSONException jsone) {
			Log.e("OgspyUtils", "Probleme d'interpretation des donnees recuperees !");
		}
		return null;
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
