package com.coded2.nabuwatercoach;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.coded2.UtilNotification;

/**
 * Created by rogerioso on 07/05/2015.
 */
public class SettingsWaterCoachFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    private int DEFAULT_ML_GOAL = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        updateGoalSummary();
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

        if(key.equalsIgnoreCase(getString(R.string.pref_key_enable_notification)) ||
                key.startsWith(getString(R.string.pref_key_notification_end_time)) ||
                key.startsWith(getString(R.string.pref_key_notification_start_time))||
                key.equalsIgnoreCase(getString(R.string.pref_key_notification_interval))){

            boolean enabled = sharedPreferences.getBoolean(getString(R.string.pref_key_enable_notification),false);

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
        String summary = getResources().getString(R.string.goal_summary);
        summary = summary.replace("#GOAL#", currentGoal);
        preference.setSummary(summary);

    }
}
