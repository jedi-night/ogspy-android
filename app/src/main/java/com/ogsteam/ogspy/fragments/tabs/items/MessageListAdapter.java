package com.ogsteam.ogspy.fragments.tabs.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ogsteam.ogspy.R;

import java.util.ArrayList;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class MessageListAdapter extends BaseAdapter {

    private ArrayList<MessageItem> listData;

    private LayoutInflater layoutInflater;

    public MessageListAdapter(Context context, ArrayList<MessageItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.messages_list, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.message_time);
            holder.sender = (TextView) convertView.findViewById(R.id.message_sender);
            holder.message = (TextView) convertView.findViewById(R.id.message_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(listData.get(position).getDate());
        holder.sender.setText(listData.get(position).getSender());
        holder.message.setText(listData.get(position).getMessage());

        return convertView;
    }

    static class ViewHolder {
        TextView date;
        TextView sender;
        TextView message;
    }

}