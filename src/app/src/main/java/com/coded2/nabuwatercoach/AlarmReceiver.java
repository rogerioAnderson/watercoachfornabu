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
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.interfaces.SendNotificationListener;
import com.razer.android.nabuopensdk.models.NabuNotification;
import com.razer.android.nabuopensdk.models.Scope;

import java.util.Date;

import static com.coded2.UtilNotification.checkStopAlarm;
import static com.coded2.UtilNotification.stopAlarm;

/**
 * Created by Rogerio on 22/05/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        try{

            Log.d(Constants.APPLICATION_TAG, "Instanciando sdk");
            final NabuOpenSDK nabuSDK = NabuOpenSDK.getInstance(context);
            /*nabuSDK.initiate(context, context.getString(R.string.razer_app_id), new String[]{Scope.COMPLETE}, new NabuAuthListener() {
                @Override
                public void onAuthSuccess(String s) {
                    Log.d(Constants.APPLICATION_TAG, "Authentication success "+s);
                    sendNotification(context,nabuSDK);
                }

                @Override
                public void onAuthFailed(String s) {
                    String msg = "Auth failed: " + s;
                    Log.d(Constants.APPLICATION_TAG, msg);
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }
            });
            */

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

        boolean shouldStopAlarm = checkStopAlarm(context);
        if(shouldStopAlarm){
            stopAlarm(context);
        }
    }
}
