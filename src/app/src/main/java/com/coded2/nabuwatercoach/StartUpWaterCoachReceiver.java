package com.coded2.nabuwatercoach;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.coded2.UtilNotification;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.coded2.CustomTimePreference.DEFAULT_HOUR;
import static com.coded2.CustomTimePreference.DEFAULT_MINUTE;
import static com.coded2.CustomTimePreference.HOUR_SUFIX;
import static com.coded2.CustomTimePreference.MINUTE_SUFIX;

/**
 * Created by rogerioso on 22/05/2015.
 */
public class StartUpWaterCoachReceiver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        if(Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())){
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
            boolean noficationEnabled = prefs.getBoolean(context.getString(R.string.pref_key_enable_notification),false);
            if(noficationEnabled){
                UtilNotification.shceduleNextNotification(context);

            }
        }
    }


}
