package com.ogsteam.ogspy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.ogsteam.ogspy.data.models.Message;
import com.ogsteam.ogspy.notification.NotificationProvider;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.permission.ServerUtilities;
import com.ogsteam.ogspy.utils.helpers.Constants;

import java.util.Date;

import static com.ogsteam.ogspy.permission.CommonUtilities.SENDER_ID;

public class GCMIntentServiceOld extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentServiceOld() {
        super(SENDER_ID);
        setIntentRedelivery(true);
    }

    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        CommonUtilities.displayMessage(context, "Your device registred with GCM");
        if (OgspyActivity.activity.getSelectedAccount() != null) {
            ServerUtilities.register(context, OgspyActivity.activity.getSelectedAccount().getUsername(), registrationId);
        }
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        if (OgspyActivity.activity.getSelectedAccount() != null) {
            ServerUtilities.unregister(context, OgspyActivity.activity.getSelectedAccount().getUsername(), registrationId);
        }
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("message");
        String messagetype = intent.getExtras().getString("messagetype");
        String sender = intent.getExtras().getString("sender");

        //CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification(context, message, messagetype, sender);
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        //CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification(context, message, null, null);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, String messageType, String sender) {
        NotificationProvider notifProvider = OgspyActivity.getNotifProvider();
        if (notifProvider != null) {
            if (messageType != null && Constants.NOTIFICATION_TYPE_HOSTILES.equals(messageType)) {
                notifProvider.createNotificationHostile(message);
            } else if (messageType != null && Constants.NOTIFICATION_TYPE_MESSAGE.equals(messageType)) {
                notifProvider.createNotificationMessage(message, sender);
                OgspyActivity.handlerMessages.addMessage(new Message(OgspyActivity.handlerMessages.getNextMessageId(), String.valueOf(new Date().getTime()), sender, message));
            }
        }


        /*int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
         
        String title = context.getString(R.string.app_name);
         
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);     */

    }

}