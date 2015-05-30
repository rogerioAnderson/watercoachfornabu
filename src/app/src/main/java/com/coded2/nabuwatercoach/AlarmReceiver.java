package com.coded2.nabuwatercoach;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.coded2.DialogManager;
import com.coded2.Util;
import com.coded2.UtilNotification;
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.interfaces.SendNotificationListener;
import com.razer.android.nabuopensdk.models.NabuNotification;
import com.razer.android.nabuopensdk.models.Scope;

import java.util.Date;
import java.util.List;

import static com.coded2.UtilNotification.checkStopAlarm;
import static com.coded2.UtilNotification.stopAlarm;

/**
 * Created by Rogerio on 22/05/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        try{

            Log.d(Constants.APPLICATION_TAG, "Alarm Received");

            final NabuOpenSDK nabuSDK = NabuOpenSDK.getInstance(context);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int currentScore = 0;

            List<DailyRecord> records = DailyRecord.listOFDay(context);

            for (DailyRecord record: records){
                currentScore+=record.ml;
            }


            String defValue = context.getString(R.string.default_water_ml_goal);
            String keyGoal = context.getString(R.string.pref_key_goal);
            int goal = Integer.parseInt(prefs.getString(keyGoal, defValue));

            if(currentScore>=goal){
                Log.w(Constants.APPLICATION_TAG,"daily goal1 achived");

                shouldStopAlarm(context);

                return;
            }



            sendNotification(context,nabuSDK);

        }catch(Exception e){
                Log.e(Constants.APPLICATION_TAG,e.getMessage());
                Toast.makeText(context,"Send notification failed: "+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification(Context context, NabuOpenSDK nabuSDK) {

        Log.d(Constants.APPLICATION_TAG,"Sending Notification");
        nabuSDK.sendNotification(context, new NabuNotification(context.getString(R.string.app_name), context.getString(R.string.notification_line)), new SendNotificationListener() {

            @Override
            public void onSuccess(String s) {
                Log.i(Constants.APPLICATION_TAG,s+" Notification sent at: "+ new Date(System.currentTimeMillis()));
            }

            @Override
            public void onFailed(String s) {
                Log.e(Constants.APPLICATION_TAG,"Notification failed: "+s);
            }
        });

        shouldStopAlarm(context);
    }

    private void shouldStopAlarm(Context context) {
        boolean shouldStopAlarm = checkStopAlarm(context);
        if(shouldStopAlarm){
            // it´s true the end time has come
            stopAlarm(context);
            // then schedule alarm to next day
            UtilNotification.shceduleNextNotification(context);
        }
    }
}
