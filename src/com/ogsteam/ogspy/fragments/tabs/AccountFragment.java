package com.ogsteam.ogspy.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.preferences.Accounts;


/**
 * @author mwho
 *
 */
public class AccountFragment extends Fragment {
    private static EditText user;
    private static EditText password;
    private static EditText serverUrl;
    private static EditText serverUniverse;

    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.accounts, container, false);

        user = (EditText) layout.findViewById(R.id.ogspy_user);
        password = (EditText) layout.findViewById(R.id.ogspy_password);
        serverUrl = (EditText) layout.findViewById(R.id.ogspy_server_url);
        serverUniverse = (EditText) layout.findViewById(R.id.ogspy_server_universe);

        Accounts.showAccount((OgspyActivity) getActivity(), this);

        return layout;
    }

    public static EditText getUser() {
        return user;
    }

    public static EditText getPassword() {
        return password;
    }

    public static EditText getServerUrl() {
        return serverUrl;
    }

    public static EditText getServerUniverse() {
        return serverUniverse;
    }
}
