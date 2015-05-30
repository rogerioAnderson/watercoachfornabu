package com.coded2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coded2.nabuwatercoach.R;


public class CustomNumberPreference extends DialogPreference{

    private static final String NAMESPACE = "http://schemas.android.com/com.coded2";
    private static final String MIN_VALUE_ATTR_NAME = "minValue";
    private static final String MAX_VALUE_ATTR_NAME = "maxValue";
    public static final int DEF_MINUTES_INTERVAL =  30;
    private NumberPicker numberPicker;
    View view;
    int minValue;
    int maxValue;

    public CustomNumberPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        minValue = attrs.getAttributeIntValue(NAMESPACE,MIN_VALUE_ATTR_NAME,minValue);
        maxValue = attrs.getAttributeIntValue(NAMESPACE,MAX_VALUE_ATTR_NAME,maxValue);
    }

    @Override
    protected void onBindDialogView(View view) {
        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker_pref);
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(getSharedPreferences().getInt(getKey(), DEF_MINUTES_INTERVAL));

    }

    @Override
    public void onClick(DialogInterface dialog, int button) {
        SharedPreferences.Editor editor = getEditor();
        if(button== Dialog.BUTTON_POSITIVE){
            editor.putInt(getKey(),numberPicker.getValue());
            editor.commit();
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
        int value =  getSharedPreferences().getInt(getKey(),DEF_MINUTES_INTERVAL);
        if(view !=null){
            TextView TxtValue = (TextView) view.findViewById(R.id.pref_view_value);
            TxtValue.setText(Integer.toString(value));
        }
    }

}
