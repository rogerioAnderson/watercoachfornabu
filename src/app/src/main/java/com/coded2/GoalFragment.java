package com.coded2;

import android.animation.Animator;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.coded2.nabuwatercoach.R;
import com.coded2.nabuwatercoach.WaterDailyRecord;
import com.razer.android.nabuopensdk.NabuOpenSDK;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rogerioso on 03/06/2015.
 */
public class GoalFragment extends Fragment {






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NabuOpenSDK nabuSDK = NabuOpenSDK.getInstance(getActivity());

        View view = inflater.inflate(R.layout.fragment_goals,container,false);


        View waterProgressView = view.findViewById(R.id.water_progress);
        //View stepsProgressView = view.findViewById(R.id.steps_progress);

        initWaterProgress(waterProgressView);

        //initStepsProgres(stepsProgressView,nabuSDK);

        return view;
    }

    private void initStepsProgres(View view, NabuOpenSDK nabuSDK) {
        ArcProgress stepsProgress = (ArcProgress) view.findViewById(R.id.progress);
        TextView stepsTitle = (TextView) view.findViewById(R.id.title);




    }

    private void initWaterProgress(final View view) {

        final ArcProgress waterProgress;
        final TextView waterTitle;

        final Timer waterTimer;

        waterProgress = (ArcProgress) view.findViewById(R.id.progress);
        waterTitle = (TextView) view.findViewById(R.id.title);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String defValue = getActivity().getString(R.string.default_water_ml_goal);
        String keyGoal = getActivity().getString(R.string.pref_key_goal);

        final int goal = Integer.parseInt(prefs.getString(keyGoal, defValue));

        long currentScore = 0;

        List<WaterDailyRecord> records = WaterDailyRecord.listOFDay(getActivity());

        for (WaterDailyRecord record: records){
            currentScore+=record.ml;
        }

        final long percent = Util.calculoPercentual(currentScore, goal);

        waterTimer = new Timer();
        final long finalCurrentScore = currentScore;
        TimerTask updateWaterProgressTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(waterProgress.getProgress()==percent){
                            waterTimer.cancel();
                            TextView waterProgressTxt = (TextView) view.findViewById(R.id.progressTxt);
                            waterTitle.setText(R.string.water);
                            waterProgressTxt.setText(finalCurrentScore + "/" + goal + " ml");
                            return;
                        }
                        waterProgress.setProgress(waterProgress.getProgress()+1);
                    }
                });
            }
        };

        waterTimer.schedule(updateWaterProgressTask, 1000, 10);

        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        in.setDuration(1000);
    }
}
