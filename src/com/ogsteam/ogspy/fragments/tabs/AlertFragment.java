package com.ogsteam.ogspy.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ogsteam.ogspy.R;


/**
 * @author mwho
 *
 */
public class AlertFragment extends Fragment {
    private static EditText message;
    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.alert, container, false);

        message =  (EditText) layout.findViewById(R.id.alertMessage);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public EditText getMessage() {
        return message;
    }
}
