package com.coded2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coded2.nabuwatercoach.R;

/**
 * Created by Rogerio on 19/05/2015.
 */
public class CustomTimePreference extends DialogPreference{

    public static final int DEFAULT_HOUR = 0;
    public static final int DEFAULT_MINUTE=0;
    public static final String HOUR_SUFIX = ".hour";
    public static final String MINUTE_SUFIX = ".minute";
    private TimePicker timePicker;
    View view;

    public CustomTimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        timePicker = (TimePicker) view.findViewById(R.id.time_picker_pref);
        timePicker.setCurrentHour(getSharedPreferences().getInt(getKey() + HOUR_SUFIX, DEFAULT_HOUR));
        timePicker.setCurrentMinute(getSharedPreferences().getInt(getKey() + MINUTE_SUFIX, DEFAULT_MINUTE));
        timePicker.setIs24HourView(DateFormat.is24HourFormat(timePicker.getContext()));

    }

    @Override
    public void onClick(DialogInterface dialog, int button) {
        SharedPreferences.Editor editor = getEditor();
        if(button== Dialog.BUTTON_POSITIVE){
            editor.putInt(getKey()+ HOUR_SUFIX,timePicker.getCurrentHour());
            editor.putInt(getKey() + MINUTE_SUFIX, timePicker.getCurrentMinute());
            editor.commit();
            Preference pref = getPreferenceManager().findPreference(getKey());
            updateValue();
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        this.view = view;
        updateValue();
    }

    private void updateValue() {
        int hour = getSharedPreferences().getInt(getKey() + HOUR_SUFIX, DEFAULT_HOUR);
        int minute = getSharedPreferences().getInt(getKey() + ".minute", DEFAULT_MINUTE);
        if(view !=null){
            TextView value = (TextView) view.findViewById(R.id.pref_view_value);
            String time;
            String hourStr = Integer.toString(hour);
            String minuteStr = Integer.toString(minute);

            if(hour<10){
                time = "0"+hourStr;
            }else{
                time=hourStr;
            }

            time+=":";

            if(minute<10){
                time+="0"+minuteStr;
            }else{
                time+=minuteStr;
            }
            value.setText(time);
        }
    }
}
