package com.coded2.nabuwatercoach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rogerioso on 12/05/2015.
 */
public class RegistryAdapter extends ArrayAdapter<WaterDailyRecord> {

    public RegistryAdapter(Context context, int resource, List<WaterDailyRecord> items) {
        super(context, resource,items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.registry,null);
        }

        WaterDailyRecord record = getItem(position);
        if(record!=null){
            TextView value = (TextView) view.findViewById(R.id.value);
            value.setText(Integer.toString(record.ml)+" ml");
            TextView time = (TextView) view.findViewById(R.id.item_time);
            time.setText(record.time.substring(0,5));
        }
        return view;
    }

}
