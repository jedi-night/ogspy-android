package com.ogsteam.ogspy.network.post;

import android.content.Context;

import com.ogsteam.ogspy.permission.CommonUtilities;

/**
 * Created by jp.tessier on 10/10/13.
 */
public class SendMessage extends PostingTask {

    public SendMessage(Context activity, String url) {
        super(activity,url);
    }

    protected void onPostExecute(String result) {
        boolean success = result.contains("success");
        if(success){
            int nbDestinataires = Integer.parseInt(result.split("success")[1].split(":")[1].split(",")[0]);
            if(nbDestinataires > 0){
                CommonUtilities.displayMessage(activity, "Message envoyé à " + nbDestinataires + " membres");
            } else {
                CommonUtilities.displayMessage(activity, "Aucun membre n'a recu votre message !");
            }
        } else {
            CommonUtilities.displayMessage(activity, "Echec lors de l'envoi du message !\n" + result);
        }
    }
}
