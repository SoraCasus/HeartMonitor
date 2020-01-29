package com.cmput301A1.heartmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Data entry adapter adapted from StackOverflow https://stackoverflow.com/a/29517252 answered
 * by Alejandro Cumpa
 */
public class DataEntryAdapter extends BaseAdapter {
    private Context context;

    private List<DataEntry> dataEntryList;
    private LayoutInflater inflater;

    public DataEntryAdapter(Context context, List<DataEntry> dataEntryList) {
        this.context = context;
        this.dataEntryList = dataEntryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataEntryList.size();
    }

    @Override
    public DataEntry getItem(int position) {
        return dataEntryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.content, parent, false);

            holder.header = convertView.findViewById(R.id.data_header);
            holder.subTitle = convertView.findViewById(R.id.data_subtext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DataEntry data = dataEntryList.get(position);
        holder.header.setText(data.getHeader());
        holder.subTitle.setText(data.getSubText());

        return convertView;
    }

    private class ViewHolder {
        TextView header;
        TextView subTitle;
    }

}
