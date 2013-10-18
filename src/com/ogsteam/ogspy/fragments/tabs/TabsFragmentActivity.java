package com.ogsteam.ogspy.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ogsteam.ogspy.R;


/**
 * @author mwho
 *
 */
public class TabsFragmentActivity extends FragmentActivity {
    /* Tab identifiers */
    static String TAB_A = "1 Tab";
    static String TAB_B = "2 Tab";
    static String TAB_C = "3 Tab";
    static String TAB_D = "4 Tab";

    TabHost mTabHost;

    GeneralFragment fragmentGeneral;
    HostileFragment fragmentHostile;
    RentabilitesFragment fragmentRentabilites;
    AlertFragment fragmentAlert;
    ConnectionFragment fragmentConnection;

    Fragment lastFragment;

    /*AccountFragment fragmentAccount;
    PrefsFragment fragmentPrefs;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ogspy_tab_host);

        TAB_A = getApplicationContext().getResources().getString(R.string.general);
        TAB_B = getApplicationContext().getResources().getString(R.string.hostiles);
        TAB_C = getApplicationContext().getResources().getString(R.string.rentabilites);
        TAB_D = "Messages";

        fragmentGeneral = new GeneralFragment();
        fragmentHostile = new HostileFragment();
        fragmentRentabilites = new RentabilitesFragment();
        fragmentAlert = new AlertFragment();
        fragmentConnection = new ConnectionFragment();

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(listener);
        mTabHost.setup();

        initializeTab();
    }

    /*
     * Initialize the tabs and set views and identifiers for the tabs
     */
    public void initializeTab() {

        TabHost.TabSpec spec    =   mTabHost.newTabSpec(TAB_A);
        mTabHost.setCurrentTab(-5);

        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(TAB_A, R.drawable.ic_tab_general_selected));
        mTabHost.addTab(spec);

        spec =   mTabHost.newTabSpec(TAB_B);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(TAB_B, R.drawable.ic_tab_hostiles_selected));
        mTabHost.addTab(spec);


        spec =   mTabHost.newTabSpec(TAB_C);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(TAB_C, R.drawable.ic_tab_rentabilites_selected));
        mTabHost.addTab(spec);

        spec =   mTabHost.newTabSpec(TAB_D);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(TAB_D, R.drawable.ic_tab_alert_selected));
        mTabHost.addTab(spec);
    }

    /*
     * TabChangeListener for changing the tab when one of the tabs is pressed
     */
    TabHost.OnTabChangeListener listener    =   new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {
            /*Set current tab..*/
            if(tabId.equals(TAB_A)){
                pushFragments(tabId, fragmentGeneral);
                lastFragment = fragmentGeneral;
            }else if(tabId.equals(TAB_B)){
                pushFragments(tabId, fragmentHostile);
                lastFragment = fragmentHostile;
            }else if(tabId.equals(TAB_C)){
                pushFragments(tabId, fragmentRentabilites);
                lastFragment = fragmentRentabilites;
            }else if(tabId.equals(TAB_D)){
                pushFragments(tabId, fragmentAlert);
                lastFragment = fragmentAlert;
            }
        }
    };

    /*
     * adds the fragment to the FrameLayout
     */
    public void pushFragments(String tag, Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        ft.replace(android.R.id.tabcontent, fragment);
        ft.commit();
    }

    /*
     * returns the tab view i.e. the tab icon and text
     */
    private View createTabView(final String text, final int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView =   (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        ((TextView) view.findViewById(R.id.tab_text)).setText(text);
        return view;
    }

    public AlertFragment getFragmentAlert() {
        return fragmentAlert;
    }

    public ConnectionFragment getFragmentConnection() {
        return fragmentConnection;
    }

    public Fragment getLastFragment() {
        if (lastFragment == null) {
            lastFragment = fragmentGeneral;
        }
        return lastFragment;
    }

    public RentabilitesFragment getFragmentRentabilites() {
        return fragmentRentabilites;
    }
}