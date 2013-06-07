package com.ogsteam.ogspy.permission;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.network.PostingTask;
import com.ogsteam.ogspy.utils.StringUtils;

import java.util.Random;

import static com.ogsteam.ogspy.permission.CommonUtilities.TAG;
import static com.ogsteam.ogspy.permission.CommonUtilities.displayMessage;

public final class ServerUtilities {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
 
    /**
     * Register this account/device pair within the server.
     *
     */
    public static void register(final Context context, String name, final String regId) {
        Log.i(TAG, "registering device (regId = " + regId + ")");
        String serverUrl = OgspyActivity.commonUtilities.SERVER_URL_REGISTER;

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                displayMessage(context, context.getString(R.string.server_registering, i, MAX_ATTEMPTS));
                PostingTask post = new PostingTask(context, StringUtils.formatPattern(serverUrl,name,regId));
                post.execute(new String[] { "do"});
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);;
                CommonUtilities.displayMessage(context, message);
                return;
            } catch (Exception e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error, MAX_ATTEMPTS);
        CommonUtilities.displayMessage(context, message);
    }
 
    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String name, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = OgspyActivity.commonUtilities.SERVER_URL_REGISTER + "&unregister=1";
        PostingTask post = new PostingTask(context, StringUtils.formatPattern(serverUrl, name, regId));
        try {
            post.execute(new String[]{"do"});
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            CommonUtilities.displayMessage(context, message);
        } catch (Exception e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error, e.getMessage());
            CommonUtilities.displayMessage(context, message);
        }
    }
}
