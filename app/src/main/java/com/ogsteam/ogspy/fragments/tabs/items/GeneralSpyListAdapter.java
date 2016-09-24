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
public class GeneralSpyListAdapter extends BaseAdapter {

    private ArrayList<GeneralSpyItem> listData;

    private LayoutInflater layoutInflater;

    public GeneralSpyListAdapter(Context context, ArrayList<GeneralSpyItem> listData) {
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
            convertView = layoutInflater.inflate(R.layout.general_spys_list, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.general_spy_list_name);
            holder.number = (TextView) convertView.findViewById(R.id.general_spy_list_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(listData.get(position).getName());
        holder.number.setText(listData.get(position).getNumber());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView number;
    }

}