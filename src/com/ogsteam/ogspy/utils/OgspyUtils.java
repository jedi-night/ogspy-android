package com.ogsteam.ogspy.utils;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.utils.helpers.HostilesHelper;
import com.ogsteam.ogspy.utils.security.MD5;
import com.ogsteam.ogspy.utils.security.SHA1;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class OgspyUtils {
    private static LinkedHashMap<String, String> serveurs = new LinkedHashMap<String, String>();

    public static void init() {
        serveurs.put("s101-fr.ogame.gameforge.com", "Andromeda");
        serveurs.put("s102-fr.ogame.gameforge.com", "Barym");
        serveurs.put("s103-fr.ogame.gameforge.com", "Capella");
        serveurs.put("s104-fr.ogame.gameforge.com", "Draco");
        serveurs.put("s105-fr.ogame.gameforge.com", "Electra");
        serveurs.put("s106-fr.ogame.gameforge.com", "Fornax");
        serveurs.put("s107-fr.ogame.gameforge.com", "Gemini");
        serveurs.put("s108-fr.ogame.gameforge.com", "Hydra");
        serveurs.put("s109-fr.ogame.gameforge.com", "Io");
        serveurs.put("s110-fr.ogame.gameforge.com", "Jupiter");
        serveurs.put("s111-fr.ogame.gameforge.com", "Kassiopeia");
        serveurs.put("s112-fr.ogame.gameforge.com", "Leo");
        serveurs.put("s113-fr.ogame.gameforge.com", "Mizar");
        serveurs.put("s114-fr.ogame.gameforge.com", "Nekkar");
        serveurs.put("s115-fr.ogame.gameforge.com", "Orion");
        serveurs.put("s116-fr.ogame.gameforge.com", "Pegasus");
        serveurs.put("s117-fr.ogame.gameforge.com", "Quantum");
        serveurs.put("s118-fr.ogame.gameforge.com", "Rigel");
        serveurs.put("s119-fr.ogame.gameforge.com", "Sirius");
        serveurs.put("s120-fr.ogame.gameforge.com", "Taurus");
        serveurs.put("s121-fr.ogame.gameforge.com", "Ursa");
        serveurs.put("s122-fr.ogame.gameforge.com", "Vega");
        serveurs.put("s123-fr.ogame.gameforge.com", "Wasat");
        serveurs.put("s124-fr.ogame.gameforge.com", "Xalynth");
        serveurs.put("s125-fr.ogame.gameforge.com", "Yakini");
        serveurs.put("s1-fr.ogame.gameforge.com", "1. univers");
        serveurs.put("s10-fr.ogame.gameforge.com", "10. univers");
        serveurs.put("s50-fr.ogame.gameforge.com", "50. univers");
        serveurs.put("s60-fr.ogame.gameforge.com", "60. univers");
        serveurs.put("s64-fr.ogame.gameforge.com", "64. univers");
        serveurs.put("s65-fr.ogame.gameforge.com", "65. univers");
        serveurs.put("s66-fr.ogame.gameforge.com", "66. univers");
        serveurs.put("s67-fr.ogame.gameforge.com", "67. univers");
        serveurs.put("s68-fr.ogame.gameforge.com", "68. univers");
    }

    public static String enryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return MD5.toMD5(SHA1.toSHA1(password));
    }

    public static boolean checkUserAccount(String username, String password, String serverUrl) {
        boolean status = true;
        StringBuilder sb = new StringBuilder();

        if (username == null || username.length() == 0) {
            status = false;
            sb.append("Le nom d'utilisateur est obligatoire !");
        }
        if (password == null || password.length() == 0) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append("Le mot de passe est obligatoire !");
        }
        if (serverUrl == null || !serverUrl.matches("http://.*")) {
            status = false;
            if (sb.toString().length() > 0) {
                sb.append("\n");
            }
            sb.append("L'adresse du serveur OGSPY est obligatoire, doit être cohérente (http://monserveur/monogspy) !");
        }
        if (sb.toString().length() > 0) {
            CommonUtilities.displayMessage(OgspyActivity.activity, sb.toString());
            //throw new OgspyException(sb.toString(), Constants.EXCEPTION_SAISIE);
        }
        return status;
    }

    public static String traiterReponseHostiles(HostilesHelper helperHostile, OgspyActivity activity) {
        //NotificationProvider notifProvider = activity.getNotifProvider();
        if (helperHostile.isAttack()) {
            TreeMap<String, ArrayList<HostilesHelper.Cible>> attaques = helperHostile.getAttaques();

            StringBuilder retour = new StringBuilder(activity.getString(R.string.hostiles_attack));
            //StringBuilder notifHostile = new StringBuilder("");
            for (Iterator<String> user = attaques.keySet().iterator(); user.hasNext(); ) {
                String userAttack = user.next();
                retour.append("\n\t- ").append(userAttack);
                for (HostilesHelper.Cible cible : attaques.get(userAttack)) {
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

    public static String getUniversNameFromUrl(String url) {
        return serveurs.get(url.replace("http://", ""));
    }

    public static int getUniversPositionFromUrl(String url) {
        int compteur = 0;
        for (Map.Entry<String, String> entry : serveurs.entrySet()) {
            String key = entry.getKey();
            if (url.replace("http://", "").equals(key)) {
                return compteur;
            }
            compteur++;
        }
        return 0;
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
