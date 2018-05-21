package com.example.cool.patient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Udayasri on 20-02-2018.
 */

public class TimingAdapter extends ArrayAdapter<String> {

    int v;
    String[] time_list;
    Context context;

    public TimingAdapter(Context context, int v, int id, String[] time_list)
    {
        super(context,v,id,time_list);
        this.context = context;
        this.time_list = time_list;
        this.v = v;
    }

    static class ViewHolder
    {
        public TextView txtEarlyMorning;
        public TextView txtMorning;
        public TextView txtAfternoon;
        public TextView txtEvening;
        public TextView txtNight;
    }

    public View getView(int position, View convertview, ViewGroup parent)
    {
        View rowView = convertview;
        if(rowView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(v,parent,false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtEarlyMorning = (TextView)rowView.findViewById(R.id.earlymorning);
            viewHolder.txtMorning = (TextView)rowView.findViewById(R.id.morning);
            viewHolder.txtAfternoon = (TextView)rowView.findViewById(R.id.afternoon);
            viewHolder.txtEvening = (TextView)rowView.findViewById(R.id.evening);
            viewHolder.txtNight = (TextView)rowView.findViewById(R.id.night);
            rowView.setTag(viewHolder);
        }
        String[] items = time_list[position].split("_");
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.txtEarlyMorning.setText(items[0]);
        viewHolder.txtEvening.setTextColor(Color.GREEN);
        viewHolder.txtMorning.setText(items[1]);
        viewHolder.txtAfternoon.setText(items[2]);
        viewHolder.txtEvening.setText(items[3]);
        viewHolder.txtNight.setText(items[4]);

        return rowView;
    }
}
