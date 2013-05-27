package com.ogsteam.ogspy.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.utils.StringUtils;

public class NotificationProvider {

	public static boolean notifHostilesAlreadyDone = false; 
	private int NOTIFICATION_ID_HOSTILES = 0;
	private Activity activity;
	
	public NotificationProvider(Activity activity){
		this.activity = activity;
	}
	
	public final void createNotificationHostile(String details){
		final NotificationManager mNotification = (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);

	    final Intent launchNotifiactionIntent = new Intent(activity, OgspyActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, launchNotifiactionIntent, PendingIntent.FLAG_ONE_SHOT);

		// Notification Icons		
		Bitmap largeIcon = ((BitmapDrawable) activity.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
		int smallIcon = R.drawable.hostiles_simple_attack;
		
		// Notifications details
		String notificationTitle = "OGSPY";
		String notificationContent = activity.getResources().getString(R.string.notification_title);
		
		Notification notif = new NotificationCompat.Builder(activity)
			.setWhen(System.currentTimeMillis())
			.setLargeIcon(largeIcon)
			.setSmallIcon(smallIcon)
			.setTicker(notificationTitle)
			.setContentTitle(notificationContent)
			.setContentText(getTextNotificationHostiles(details))
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.setDefaults(Notification.DEFAULT_SOUND) // Play default notification sound
			.setDefaults(Notification.DEFAULT_VIBRATE) // Vibrate if vibrate is enabled
			.build();
		
		mNotification.notify(NOTIFICATION_ID_HOSTILES, notif);
	}
	
	public void deleteNotificationHostile(){
    	final NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
    	//la suppression de la notification se fait grâce a son ID
    	notificationManager.cancel(NOTIFICATION_ID_HOSTILES);
    }
	
	private String getTextNotificationHostiles(String details){
		return StringUtils.formatPattern(activity.getResources().getString(R.string.notification_desc),details);
	}
}
