package com.ogsteam.ogspy.network;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.fragments.tabs.HostileItem;
import com.ogsteam.ogspy.fragments.tabs.HostilesListAdapter;
import com.ogsteam.ogspy.server.HostilesHelper;
import com.ogsteam.ogspy.utils.Constants;
import com.ogsteam.ogspy.utils.HttpUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;
import com.ogsteam.ogspy.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DownloadTask extends AsyncTask<String, Integer, String> {
    private OgspyActivity activity;
	protected static String hostilesData;
	protected static JSONObject dataJsonFromAsyncTask;
    protected HostilesHelper helperHostile;

	public DownloadTask(OgspyActivity activity) {
        this.activity = activity;
    }

    @Override
	protected String doInBackground(String... params) {
		try {
			if(!activity.getHandlerAccount().getAllAccounts().isEmpty()){
				Account account = activity.getHandlerAccount().getAccountById(0);
				String url = StringUtils.formatPattern(Constants.URL_GET_OGSPY_INFORMATION, account.getServerUrl(), account.getUsername(), OgspyUtils.enryptPassword(account.getPassword()), account.getServerUnivers());
				String data = HttpUtils.getUrl(url);
				dataJsonFromAsyncTask = new JSONObject(data.replaceAll("[(]", "").replaceAll("[)]", ""));
                helperHostile = new HostilesHelper(dataJsonFromAsyncTask);
				hostilesData = OgspyUtils.traiterReponseHostiles(helperHostile, activity);
			}
		} catch (Exception e) {
			Log.e(OgspyActivity.DEBUG_TAG, activity.getString(R.string.download_problem),e);
		}
		return null;
	}

     protected void onProgressUpdate(Integer... progress) {
         //setProgressPercent(progress[0]);
     }

	protected void onPostExecute(String result) {
		/*if(activity.findViewById(R.id.response_ogspy) != null){
			((TextView) activity.findViewById(R.id.response_ogspy)).setText(hostilesData);
		}*/

            ArrayList<HostileItem> hostileItems = new ArrayList<HostileItem>();
            for ( Iterator<String> user = helperHostile.getAttaques().keySet().iterator(); user.hasNext(); ) {
                String userAttack = user.next();
                HostileItem item = new HostileItem();
                for(HostilesHelper.Cible cible: helperHostile.getAttaques().get(userAttack)){
                    item.setTitle(userAttack, cible.getCiblePlanet(), cible.getCibleCoords());
                    item.setDate(cible.getArrivalTime());
                    item.setDetail(cible.getOriginPlanet(), cible.getOriginCoords(), cible.getAttacker());
                    item.setCompo(cible.getCompo());
                    hostileItems.add(item);
                }
            }
            final ListView lv1 = (ListView) activity.findViewById(R.id.list_view_hostiles);
            if(lv1!=null){
                lv1.setAdapter(new HostilesListAdapter(activity, hostileItems));
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        Object o = lv1.getItemAtPosition(position);
                        HostileItem hostileData = (HostileItem) o;
                        Toast.makeText(activity, hostileData.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
	}

    public static String getHostilesData() {
		return hostilesData;
	}

	public static void setHostilesData(String data) {
		DownloadTask.hostilesData = data;
	}

}
