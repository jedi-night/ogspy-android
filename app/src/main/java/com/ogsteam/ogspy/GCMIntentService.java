package com.ogsteam.ogspy;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ogsteam.ogspy.data.models.Message;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.utils.helpers.Constants;

import java.util.Date;

import static com.ogsteam.ogspy.permission.CommonUtilities.SENDER_ID;

public class GCMIntentService extends IntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            String message = extras.getString("message");
            String messagetype = extras.getString("messagetype");
            String sender = extras.getString("sender");

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                CommonUtilities.displayMessage(getApplicationContext(), getString(R.string.gcm_error, "Send error: " + extras.toString()));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                CommonUtilities.displayMessage(getApplicationContext(), getString(R.string.gcm_error, "Deleted messages on server: " + extras.toString()));
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType) || messagetype != null) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                generateNotification(message, messagetype, sender);
                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(String message, String messageType, String sender) {
        //NotificationProvider notifProvider = OgspyActivity.getNotifProvider();
        /*if (notifProvider != null) {
            if (messageType != null && Constants.NOTIFICATION_TYPE_HOSTILES.equals(messageType)) {
                notifProvider.createNotificationHostile(message);
            } else if (messageType != null && Constants.NOTIFICATION_TYPE_MESSAGE.equals(messageType)) {
                notifProvider.createNotificationMessage(message, sender);
                OgspyActivity.handlerMessages.addMessage(new Message(OgspyActivity.handlerMessages.getNextMessageId(), String.valueOf(new Date().getTime()), sender, message));
            }
        }*/
    }

}