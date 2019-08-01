package com.test.ydool.sanmen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.bean.VillageButtonBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/30.
 */

public class GridViewAdapter extends BaseAdapter{
    private Context context;
    private List<VillageButtonBean> villageButtonBeans;

    public GridViewAdapter(Context context, List<VillageButtonBean> villageButtonBeans) {
        this.context = context;
        this.villageButtonBeans = villageButtonBeans;
    }

    @Override
    public int getCount() {
        return villageButtonBeans.size();
    }
    @Override
    public Object getItem(int position) {
        return villageButtonBeans.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gradview, null);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.small_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(villageButtonBeans.get(position).getVillageName());
        return convertView;
    }
    public static class ViewHolder {
        TextView textView;
    }
}
