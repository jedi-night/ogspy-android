package com.ogsteam.ogspy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.crash.FirebaseCrash;
import com.ogsteam.ogspy.data.DatabaseAccountHandler;
import com.ogsteam.ogspy.data.DatabaseMessagesHandler;
import com.ogsteam.ogspy.data.DatabasePreferencesHandler;
import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.fragments.tabs.AlertFragment;
import com.ogsteam.ogspy.fragments.tabs.ConnectionFragment;
import com.ogsteam.ogspy.fragments.tabs.HostileFragment;
import com.ogsteam.ogspy.fragments.tabs.RentabilitesFragment;
import com.ogsteam.ogspy.fragments.tabs.SpysFragment;
import com.ogsteam.ogspy.network.ConnectionDetector;
import com.ogsteam.ogspy.network.download.DownloadAllianceTask;
import com.ogsteam.ogspy.network.download.DownloadHostilesTask;
import com.ogsteam.ogspy.network.download.DownloadRentabilitesTask;
import com.ogsteam.ogspy.network.download.DownloadServerTask;
import com.ogsteam.ogspy.network.download.DownloadSpysTask;
import com.ogsteam.ogspy.network.download.DownloadTask;
import com.ogsteam.ogspy.network.download.DownloadTokenTask;
import com.ogsteam.ogspy.network.responses.ApiToken;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.permission.ServerUtilities;
import com.ogsteam.ogspy.sliding.menu.adapter.NavDrawerListAdapter;
import com.ogsteam.ogspy.sliding.menu.model.NavDrawerItem;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.ui.displays.HostileUtils;
import com.ogsteam.ogspy.ui.displays.RentabilitesUtils;
import com.ogsteam.ogspy.ui.displays.SpysUtils;
import com.ogsteam.ogspy.utils.OgspyUtils;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OgspyActivity extends AppCompatActivity {

    public static final String TAG = OgspyActivity.class.getSimpleName();
    public static ApiToken apiToken;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.my_toolbar) Toolbar toolbar;
    @BindView(R.id.list_slidermenu) ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // drawer title
    public CharSequence mDrawerTitle;

    // used to store app title
    public CharSequence mTitle;

    // used to store ogspy server name
    public static CharSequence mServerName = "OGSpy";

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private ArrayList<Fragment> fragments;
    private Fragment lastFragment;

    public static final String versionAndroid = Build.VERSION.RELEASE;
    public static String versionOgspy = "";
    public static final String deviceName = retrieveDeviceName();

    public static int selectedMenu = 0;
    public static Timer autoUpdateHostiles;
    protected static String regId;
    private static boolean isWaiting = false;

    // Variables
    public static DatabaseAccountHandler handlerAccount;
    public static DatabasePreferencesHandler handlerPrefs;
    public static DatabaseMessagesHandler handlerMessages;

    public static CommonUtilities commonUtilities;

    public static DownloadTokenTask downloadTokenTask;
    public static DownloadServerTask downloadServerTask;
    public static DownloadHostilesTask downloadHostilesTask;
    public static DownloadAllianceTask downloadAllianceTask;
    public static DownloadSpysTask downloadSpysTask;
    public static DownloadRentabilitesTask downloadRentasTask;

    public static Spinner accountsList;
    // Connection detector
    public static ConnectionDetector connection;

    private static int lastSeletedAccountPosition = -1;

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ogspy_tab_host);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        OgspyUtils.init();

        fragments = new ArrayList<>();

        try {
            versionOgspy = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.e(TAG, "Impossible de recuperer la version ogspy", e);
        }

        connection = new ConnectionDetector(this);

        handlerAccount = new DatabaseAccountHandler(this);
        handlerPrefs = new DatabasePreferencesHandler(this);
        handlerMessages = new DatabaseMessagesHandler(this);

        fragments.add(new SpysFragment());
        fragments.add(new HostileFragment());
        fragments.add(new RentabilitesFragment());
        fragments.add(new AlertFragment());

        initializeDatas();

        setMenuSliding(savedInstanceState);

        // Check if Internet present
        if (!connection.isConnectingToInternet()) {
            connection = new ConnectionDetector(this);
            if (!connection.isConnectingToInternet()) {
                CommonUtilities.displayMessage(this, "Problem de connection : " + connection.getInfos());
                showWaiting(false);
                showConnectivityProblem(true);
            }
        }

        FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
        FirebaseCrash.report(new NullPointerException("Nullpointer"));
        // TODO : get Token from api
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//        if (accountsList == null) {
//            accountsList = (Spinner) menu.findItem(R.id.action_compte).getActionView();
//            refreshAcountsList(accountsList);
//
//
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//            if (preferences.contains("prefs_account_selected")) {
//                String idAccount = preferences.getString("prefs_account_selected", "");
//                for (int i = 0; i < accountsList.getCount(); i++) {
//                    Account acc = (Account) accountsList.getItemAtPosition(i);
//                    if (String.valueOf(acc.getId()).equals(idAccount)) {
//                        accountsList.setSelection(i);
//                        break;
//                    }
//                }
//            }
//
//            commonUtilities = new CommonUtilities(this);
//            notifProvider = new NotificationProvider(this);
//            //downloadDatas();
//            //refreshThisDatasFromResume();
//        } else {
//            accountsList = (Spinner) menu.findItem(R.id.action_compte).getActionView();
//            refreshAcountsList(accountsList);
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshThisDatasFromMenuRefresh();
                return true;
            case R.id.about:
                startActivity(new Intent(this, OgspyAboutActivity.class));
                return true;
            case R.id.prefs:
                //startActivityForResult(new Intent(this, OgspyPreferencesActivity.class), CODE_RETOUR_PREFS);
                startActivity(new Intent(this, OgspyPreferencesActivity.class));
                return true;
            case R.id.quit:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.prefs).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshThisDatasFromResume();
    }

    @Override
    public void onPause() {
        if (autoUpdateHostiles != null) autoUpdateHostiles.cancel();
        super.onPause();
    }

    private void downloadAllDatas() {
        DownloadTask.executeDownload(downloadServerTask);
        DownloadTask.executeDownload(downloadAllianceTask);
        DownloadTask.executeDownload(downloadSpysTask);
        DownloadTask.executeDownload(downloadHostilesTask);
        DownloadTask.executeDownload(downloadRentasTask);
    }

    private void refreshThisDatasFromResume() {
        if (selectedMenu == 0) {
            if (downloadSpysTask != null && downloadSpysTask.getSpysHelper() == null) {
                DownloadTask.executeDownload(downloadSpysTask);
            } else {
                SpysUtils.showSpys(null, downloadSpysTask.getSpysHelper());
            }
        } else if (selectedMenu == 1) {
            if (downloadHostilesTask != null && downloadHostilesTask.getHelperHostile() == null) {
                DownloadTask.executeDownload(downloadHostilesTask);
            } else {
                HostileUtils.showHostiles(downloadHostilesTask.getHelperHostile());
            }
        } else if (selectedMenu == 2) {
            if (downloadRentasTask != null && downloadRentasTask.getHelperRentabilites() == null) {
                DownloadTask.executeDownload(downloadRentasTask);
            } else {
                RentabilitesUtils.showRentabilites(downloadRentasTask.getHelperRentabilites(), downloadRentasTask.getType());
            }
        }
    }

    private void refreshThisDatasFromMenuRefresh() {
        if (selectedMenu == 0) {
            DownloadTask.executeDownload(downloadSpysTask);
        } else if (selectedMenu == 1) {
            DownloadTask.executeDownload(downloadHostilesTask);
        } else if (selectedMenu == 2) {
            DownloadTask.executeDownload(downloadRentasTask);
        }
    }

    public void unregisteringOgspy(View view) {
        new DialogHandler().confirm(this,
                "Désincription aux notifications",
                "Etes-vous sûr de vouloir vous désinscrire des notifications du serveur OGSPY ?",
                "Non",
                "Oui",
                new Runnable() {
                    public void run() {
                        processUnregistering();
                    }
                },
                new Runnable() {
                    public void run() {
                    }
                }
        );
    }

    public void processUnregistering() {
        if (getSelectedAccount() != null) {
            //TODO: unregister from GCM
        }
        else {
            CommonUtilities.displayMessage(this, getString(R.string.account_not_configured));
        }
    }

    public static DatabaseAccountHandler getHandlerAccount() {
        return handlerAccount;
    }

    private void initializeDatas() {
        downloadServerTask = new DownloadServerTask();
        downloadAllianceTask = new DownloadAllianceTask();
        downloadSpysTask = new DownloadSpysTask();
        downloadHostilesTask = new DownloadHostilesTask();
        downloadRentasTask = new DownloadRentabilitesTask();
    }

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        super.onDestroy();
    }

    public static String getVersionOgspy() {
        return versionOgspy;
    }

    public void sendAlert(View v) {
        if (!getHandlerAccount().getAllAccounts().isEmpty()) {
            EditText messageEditText = ((AlertFragment) getFragmentAlert()).getMessage();
            if (messageEditText.getText() != null && messageEditText.getText().length() > 0) {
                ServerUtilities.sendAlertMesage(this, regId, getSelectedAccount().getUsername(), messageEditText.getText().toString());
                messageEditText.setText("");
            } else {
                CommonUtilities.displayMessage(this, "Le message d'alerte est vide, il n'a donc pas été envoyé.");
            }
        }
    }

    public void showWaiting(boolean visible) {
        if (visible) {
            if (!isWaiting) {
                findViewById(R.id.tabcontent).setVisibility(View.GONE);
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                isWaiting = true;
            }
        } else {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.tabcontent).setVisibility(View.VISIBLE);
            isWaiting = false;
        }
    }

    private void setMenuSliding(Bundle savedInstanceState) {
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        // General
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], android.R.drawable.ic_menu_view));
        // Hostiles
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], android.R.drawable.ic_dialog_alert));
        // Rentabilites
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], android.R.drawable.ic_dialog_map));
        // Messages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], android.R.drawable.sym_action_email));
        // Pages
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(this, navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle + " - " + mServerName);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //getSupportActionBar().setIcon(((NavDrawerItem) mDrawerList.getItemAtPosition(0)).getIcon());
            displayView(0);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getSupportActionBar().setTitle(new StringBuilder(mTitle).append(" - ").append(mServerName));
    }

    public void showConnectivityProblem(boolean visible) {
        if (visible) {
            pushFragments(new ConnectionFragment());
        } else {
            pushFragments(lastFragment);
        }
    }

    public static String getRegId() {
        return regId;
    }

    public static String getDeviceName() {
        return deviceName;
    }

    public static String retrieveDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String buildNumber = Build.ID;
        if (model.startsWith(manufacturer)) {
            return model + "-" + buildNumber;
        } else {
            return manufacturer + "-" + model + "-" + buildNumber;
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
            //getSupportActionBar().setIcon(((NavDrawerItem) mDrawerList.getItemAtPosition(position)).getIcon());
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        selectedMenu = position;
        // update the main content by replacing fragments
        Fragment fragment = fragments.get(position);

        if (fragment != null) {
            pushFragments(fragment);
            lastFragment = fragment;

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            //getSupportActionBar().setIcon(((NavDrawerItem)mDrawerList.getSelectedItem()).getIcon());
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.d(TAG, "Error in creating fragment or not a fragment");
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        //getSupportActionBar().setIcon(((NavDrawerItem)mDrawerList.getSelectedItem()).getIcon());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public static DownloadHostilesTask getDownloadHostilesTask() {
        return downloadHostilesTask;
    }

    public static DownloadSpysTask getDownloadSpysTask() {
        return downloadSpysTask;
    }

    public static DownloadRentabilitesTask getDownloadRentasTask() {
        return downloadRentasTask;
    }

    public Fragment getFragmentAlert() {
        return fragments.get(3);
    }

    public void pushFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        try {
            if (fragment != null) {
                fragmentManager.beginTransaction().replace(R.id.tabcontent, fragment).commitAllowingStateLoss();
            } else if (fragment != lastFragment) {
                fragmentManager.beginTransaction().replace(R.id.tabcontent, lastFragment).commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction().replace(R.id.tabcontent, fragments.get(0)).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            CommonUtilities.displayMessage(this, "Problem d'affichage de la page : " + fragment.getClass().getSimpleName() + "\n(" + e.getMessage() + ")");
        }
    }

    public static Account getSelectedAccount() {
        if (accountsList == null || accountsList.getSelectedItem() == null) return null;
        return (Account) accountsList.getSelectedItem();
    }
}
