package com.coded2.nabuwatercoach;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

import com.coded2.CustomNumberPreference;
import com.coded2.CustomTimePreference;
import com.coded2.UtilNotification;

/**
 * Created by rogerioso on 07/05/2015.
 */
public class SettingsWaterCoachFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    private final int  DEFAULT_INTERVAL = 30;
    private final int DEFAULT_ML_GOAL = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        updateGoalSummary();
        updateTimeSummary(R.string.pref_key_notification_start_time,R.string.notifications_start_summary);
        updateTimeSummary(R.string.pref_key_notification_end_time,R.string.notifications_end_summary);
        updateIntervalSummary();

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getResources().getString(R.string.pref_key_goal))) {
            updateGoalSummary();
            return;
        }

        updateTimeSummary(R.string.pref_key_notification_start_time,R.string.notifications_start_summary);
        updateTimeSummary(R.string.pref_key_notification_end_time,R.string.notifications_start_summary);
        updateIntervalSummary();

        if(key.equalsIgnoreCase(getString(R.string.pref_key_enable_notification)) ||
                key.startsWith(getString(R.string.pref_key_notification_end_time)) ||
                key.startsWith(getString(R.string.pref_key_notification_start_time))||
                key.equalsIgnoreCase(getString(R.string.pref_key_notification_interval))){

            boolean enabled = sharedPreferences.getBoolean(getString(R.string.pref_key_enable_notification), false);


            if(enabled){
                UtilNotification.shceduleNextNotification(getActivity());
            }else{
                UtilNotification.stopAlarm(getActivity());
            }
            return;
        }



    }

    private void updateGoalSummary() {
        String key = getResources().getString(R.string.pref_key_goal);
        Preference preference = getPreferenceManager().findPreference(key);
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        String currentGoal = preferences.getString(key, Integer.toString(DEFAULT_ML_GOAL));
        if(currentGoal.isEmpty()){
            currentGoal = Integer.toString(DEFAULT_ML_GOAL);
            preferences.edit().putString(key,currentGoal).commit();
        }
        String summary = getResources().getString(R.string.goal_summary);
        summary = summary.replace("#GOAL#", currentGoal);
        preference.setSummary(summary);

    }


    private void updateIntervalSummary() {
        String key = getResources().getString(R.string.pref_key_notification_interval);
        Preference preference = getPreferenceManager().findPreference(key);
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        int currentGoal = preferences.getInt(key, DEFAULT_INTERVAL);
        String summary = getResources().getString(R.string.notifications_interval_summary);
        summary = summary.replace("#INTERVAL#", Integer.toString(currentGoal));
        preference.setSummary(summary);

    }


    private void updateTimeSummary(int keyID,int summaryID) {

        String key = getString(keyID);
        Preference preference = getPreferenceManager().findPreference(key);
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        String summary = getString(summaryID);
        int hour =  preferences.getInt(key+".hour", 0);
        int minute =  preferences.getInt(key + ".minute", 0);

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

        preference.setSummary(summary.replace("#TIME#", time));

    }



}
