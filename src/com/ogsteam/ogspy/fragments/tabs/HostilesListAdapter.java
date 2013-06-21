package com.ogsteam.ogspy.fragments.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogsteam.ogspy.R;

import java.util.ArrayList;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class HostilesListAdapter extends BaseAdapter {

    private ArrayList<HostileItem> listData;

    private LayoutInflater layoutInflater;

    public HostilesListAdapter(Context context, ArrayList<HostileItem> listData) {
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
            convertView = layoutInflater.inflate(R.layout.hostiles_list, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.hostile_list_image);
            holder.title = (TextView) convertView.findViewById(R.id.hostile_list_title);
            holder.date = (TextView) convertView.findViewById(R.id.hostile_list_date);
            holder.detail = (TextView) convertView.findViewById(R.id.hostile_list_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(listData.get(position).isAg()){
                holder.image.setImageResource(R.drawable.hostiles_group_attack);
        }
        holder.title.setText(listData.get(position).getTitle());
        holder.date.setText(listData.get(position).getDate());
        holder.detail.setText(listData.get(position).getDetail());

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
        TextView date;
        TextView detail;
    }

}