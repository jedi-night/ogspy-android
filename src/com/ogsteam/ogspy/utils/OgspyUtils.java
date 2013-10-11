package com.ogsteam.ogspy.utils;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.utils.helpers.HostilesHelper;
import com.ogsteam.ogspy.utils.security.MD5;
import com.ogsteam.ogspy.utils.security.SHA1;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class OgspyUtils {

	public static String enryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return MD5.toMD5(SHA1.toSHA1(password));
	}

	public static String traiterReponseHostiles(HostilesHelper helperHostile, OgspyActivity activity){
		//NotificationProvider notifProvider = activity.getNotifProvider();
        if(helperHostile.isAttack()){
            TreeMap<String, ArrayList<HostilesHelper.Cible>> attaques = helperHostile.getAttaques();

            StringBuilder retour = new StringBuilder(activity.getString(R.string.hostiles_attack));
            //StringBuilder notifHostile = new StringBuilder("");
            for ( Iterator<String> user = attaques.keySet().iterator(); user.hasNext(); ) {
                String userAttack = user.next();
                retour.append("\n\t- ").append(userAttack);
                for(HostilesHelper.Cible cible: attaques.get(userAttack)){
                    retour.append("\n\t\t* ")
                            .append(cible.getAttacker()).append(" de ").append(cible.getOriginPlanet()).append(" (").append(cible.getOriginCoords()).append(")")
                            .append(" attaque ").append(cible.getCiblePlanet()).append(" (").append(cible.getCibleCoords()).append(")");
                }
            }
            /*if(!NotificationProvider.notifHostilesAlreadyDone){
                NotificationProvider.notifHostilesAlreadyDone = true;
                notifProvider.createNotificationHostile(notifHostile.toString());
            }*/
            return retour.toString();
        } else {
            NotificationProvider.notifHostilesAlreadyDone = false;
            //notifProvider.deleteNotificationHostile();
            return activity.getString(R.string.hostiles_none);
        }
	}
	/*
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
	}*/
}
