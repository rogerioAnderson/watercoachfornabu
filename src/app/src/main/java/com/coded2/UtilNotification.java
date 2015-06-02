package com.coded2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.coded2.nabuwatercoach.AlarmWaterCoachReceiver;
import com.coded2.nabuwatercoach.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.coded2.CustomTimePreference.DEFAULT_HOUR;
import static com.coded2.CustomTimePreference.DEFAULT_MINUTE;
import static com.coded2.CustomTimePreference.HOUR_SUFIX;
import static com.coded2.CustomTimePreference.MINUTE_SUFIX;

/**
 * Created by Rogerio on 22/05/2015.
 */
public class UtilNotification {


    public static void shceduleNextNotification(Context context){

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);

        int startHour = prefs.getInt(context.getString(R.string.pref_key_notification_start_time)+ HOUR_SUFIX,DEFAULT_HOUR);
        int startMinute = prefs.getInt(context.getString(R.string.pref_key_notification_start_time)+ MINUTE_SUFIX,DEFAULT_MINUTE);

        int endHour = prefs.getInt(context.getString(R.string.pref_key_notification_end_time)+ HOUR_SUFIX,DEFAULT_HOUR);
        int endMinute = prefs.getInt(context.getString(R.string.pref_key_notification_end_time)+ MINUTE_SUFIX,DEFAULT_MINUTE);

        int intervalInMinutes = prefs.getInt(context.getString(R.string.pref_key_notification_interval),30);

        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY,startHour);
        start.set(Calendar.MINUTE,startMinute);
        start.set(Calendar.SECOND,0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY,endHour);
        end.set(Calendar.MINUTE,endMinute);
        end.set(Calendar.SECOND,0);

        Calendar notificationDate;

        Calendar currentTime = new GregorianCalendar();
        currentTime.setTimeInMillis(System.currentTimeMillis());

        if(currentTime.getTime().after(end.getTime())){
            start.add(Calendar.DATE,1);
        }

        if(currentTime.getTime().before(start.getTime())){
            notificationDate = start;
        }else{
            currentTime.add(Calendar.MINUTE,intervalInMinutes);
            notificationDate = currentTime;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmWaterCoachReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(),
                minutesToMillesconds(intervalInMinutes),
                pendingIntent);

        int hour = notificationDate.get(Calendar.HOUR_OF_DAY);
        int minute = notificationDate.get(Calendar.MINUTE);
        int day = notificationDate.get(Calendar.DAY_OF_MONTH);
        int month = notificationDate.get(Calendar.MONTH);
        int year = notificationDate.get(Calendar.YEAR);

        String msg = "Next Alarm: " + day + "/" + (month + 1) + "/" + year + " as " + ((hour < 10) ? "0" + hour : hour) + ":" + ((minute < 10) ? "0" + minute : minute);
        Log.i(Constants.APPLICATION_TAG, msg);
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

    }

    private static long minutesToMillesconds(int intervalInMinutes) {
        return 1000 * 60 * intervalInMinutes;
    }

    public static void stopAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmWaterCoachReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);

        Log.i(Constants.APPLICATION_TAG,"Alarm Canceled");
    }

    public static boolean checkStopAlarm(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        int endHour = prefs.getInt(context.getString(R.string.pref_key_notification_end_time) + HOUR_SUFIX, CustomTimePreference.DEFAULT_HOUR);
        int endMinute = prefs.getInt(context.getString(R.string.pref_key_notification_end_time) + MINUTE_SUFIX, CustomTimePreference.DEFAULT_MINUTE);

        Calendar currentTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY,endHour);
        endTime.set(Calendar.MINUTE, endMinute);

        if(currentTime.after(endTime)){
            return true;
        }


        return false;
    }
}
