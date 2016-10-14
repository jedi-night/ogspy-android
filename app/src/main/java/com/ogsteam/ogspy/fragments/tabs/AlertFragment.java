package com.ogsteam.ogspy.fragments.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ogsteam.ogspy.OgspyActivity;
import com.ogsteam.ogspy.R;
import com.ogsteam.ogspy.data.models.Message;
import com.ogsteam.ogspy.fragments.tabs.items.MessageItem;
import com.ogsteam.ogspy.fragments.tabs.items.MessageListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * @author mwho
 */
public class AlertFragment extends Fragment {
    private static EditText message;
    private static ListView messages;

    /**
     * (non-Javadoc)
     *
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
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.messages, container, false);

        messages = (ListView) layout.findViewById(R.id.message_list);
        message = (EditText) layout.findViewById(R.id.alertMessage);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<MessageItem> messagesItems = new ArrayList<MessageItem>();
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMM yyyy - HH:mm", Locale.FRANCE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.FRANCE);
        for (Message message : OgspyActivity.handlerMessages.getAllMessagesDesc()) {
            messagesItems.add(new MessageItem(sdf.format(Long.parseLong(message.getDatetime())), message.getSender(), message.getContent()));
        }
        if (messages != null) {
            messages.setAdapter(new MessageListAdapter(getActivity(), messagesItems));
        }
    }

    public EditText getMessage() {
        return message;
    }
}
