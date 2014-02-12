package com.ogsteam.ogspy.utils;

import android.util.Log;

import com.ogsteam.ogspy.OgspyActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
	private static final String DEBUG_TAG = HttpUtils.class.getSimpleName();
    private static final int bufferLength = 5000;

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	public static String getUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    String contentAsString = null;
        HttpURLConnection conn = null;
	    try {
	        URL url = new URL(myurl);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(15000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(DEBUG_TAG, "The response is: " + response);
	        is = conn.getInputStream();

	        // Convert the InputStream into a string
	        contentAsString = readIt(is, bufferLength);

            OgspyActivity.activity.showConnectivityProblem(false);
            // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } catch (Exception e){
	    	Log.e(DEBUG_TAG, "Problème lors d'une connection", e);
            OgspyActivity.activity.showConnectivityProblem(true);
        } finally {
	        if (is != null) {
	            is.close();
	        }
            if(conn != null){
                conn.disconnect();
            }
	    }
        return contentAsString;
	}

    public static String getUrlWithoutDisplayConnectivityProblem(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        String contentAsString = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            contentAsString = readIt(is, bufferLength);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Problème lors d'une connection", e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return contentAsString;
    }

    // Reads an InputStream and converts it to a String.
	private static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "ISO-8859-1");
	    char[] buffer = new char[len];
	    reader.read(buffer);
        //br.read(buffer);
	    return new String(buffer);
	}
}
