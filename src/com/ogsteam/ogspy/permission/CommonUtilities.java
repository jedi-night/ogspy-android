package com.ogsteam.ogspy.permission;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;

public final class CommonUtilities {

    // give your server registration url here
    //static final String SERVER_URL_REGISTER = "http://10.0.2.2/gcm_server_php/register.php";
    static String SERVER_URL_REGISTER = "gcm/register.php?name={0}&regId={1}";
    static String SERVER_URL_ALERT = "gcm/send_alert.php?regId={0}&username={1}&message={2}";

    // Google project id
    static final public String SENDER_ID = "990785741190";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "OGSpy";

    static final public String DISPLAY_MESSAGE_ACTION = "com.ogsteam.ogspy.permission.DISPLAY_MESSAGE";

    static final public String EXTRA_MESSAGE = "message";


    public CommonUtilities(OgspyActivity activity) {
        if (!activity.getHandlerAccount().getAllAccounts().isEmpty()) {
            Account account = activity.getSelectedAccount();
            SERVER_URL_REGISTER = account.getServerUrl() + "/" + SERVER_URL_REGISTER;
            SERVER_URL_ALERT = account.getServerUrl() + "/" + SERVER_URL_ALERT;
        }
    }

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //context.sendBroadcast(intent);
    }

    public static void displayMessageDebug(Context context, String message) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefs_debug_messages", false)) {
            displayMessage(context, message);
        }
        //Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //context.sendBroadcast(intent);
    }

    public static void displayMessageDebug(Context context, Class c, String message) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefs_debug_messages", false)) {
            displayMessage(context, c.getSimpleName() + " : " + message);
        }
        //Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //context.sendBroadcast(intent);
    }

    public static void displayMessageDebugAndLog(Context context, Class c, String message, Exception e, String... extras) {
        StringBuilder debugMessage = new StringBuilder(message);
        for (String extra : extras) {
            debugMessage.append(" ").append(extra);
        }
        Log.e(c.getSimpleName(), context.getString(R.string.download_problem), e);
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefs_debug_messages", false)) {
            displayMessage(context, c.getClass().getSimpleName() + " : " + message);
        }
        //Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //context.sendBroadcast(intent);
    }

}