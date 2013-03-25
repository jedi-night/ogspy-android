package com.ogsteam.ogspy.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ogsteam.ogspy.notification.NotificationProvider;

public class OgspyUtils {

	public static String enryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return MD5.toMD5(SHA1.toSHA1(password));
	}
	
	public static String traiterReponseHostiles(String data, NotificationProvider notifProvider){
		// ({"new_messages": 0,"type": "checkhostiles","check": 0,"user": "","execution": 14.19})
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
			StringBuilder retour = new StringBuilder("Flottes hostiles en approche sur le(s) joueur(s) : ");
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
			if(!notifProvider.isNotifHostilesAlreadyDone()){
				notifProvider.setNotifHostilesAlreadyDone(true);
				notifProvider.createNotificationHostile(notifHostile.toString());
			}
			return retour.toString();
		} else {
			notifProvider.setNotifHostilesAlreadyDone(false);
			notifProvider.deleteNotificationHostile();
			return "Aucune flotte hostiles en approche.";
		}
		//return "Aucune information n'a pu être récupérée";
	}
	
}
