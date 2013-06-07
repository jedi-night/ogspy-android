package com.ogsteam.ogspy.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ogsteam.ogspy.utils.HttpUtils;

public class PostingTask extends AsyncTask<String, Integer, String> {
    private Context activity;
    private String url;
    private String result;

	public PostingTask(Context activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			result = HttpUtils.getUrl(this.url);
		} catch (Exception e) {
			Log.e("PostingTask", "Problème lors du post !",e);
		}
		return result;
	}

     protected void onProgressUpdate(Integer... progress) {
         //setProgressPercent(progress[0]);
     }

	protected void onPostExecute(String result) {
		//this.result = result;
	}
}
