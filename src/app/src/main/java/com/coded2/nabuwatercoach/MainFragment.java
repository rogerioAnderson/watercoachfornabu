package com.coded2.nabuwatercoach;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class MainFragment extends Fragment{

    TextView score ;
    ProgressBar goalBar;
    GridView dailyRecords;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        String goal = prefs.getString(getResources().getString(R.string.pref_key_goal),getString(R.string.default_water_ml_goal));

        score = (TextView) rootView.findViewById(R.id.lbl_score);
        goalBar = (ProgressBar) rootView.findViewById(R.id.goal_bar);

        goalBar.setProgressDrawable(getResources().getDrawable(R.drawable.razer_progress));
        goalBar.setMax(Integer.parseInt(goal));
        dailyRecords = (GridView) rootView.findViewById(R.id.daily_records);

        int progress = 0;
        DBHelper helper = new DBHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        List<DailyRecord> records = DailyRecord.listOFDay(getActivity());

        for (DailyRecord record: records){
            progress+=record.ml;
        }

        score.setText(progress + "/" + goal + " ml");

        goalBar.setProgress(progress);






        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getString(R.string.pref_key_current_score),progress);
        editor.commit();


        RegistryAdapter adapter = new RegistryAdapter(getActivity(),R.layout.registry, records);

        dailyRecords.setAdapter(adapter);

        dailyRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.remove_water_dialog_title);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DailyRecord record = (DailyRecord) dailyRecords.getAdapter().getItem(position);
                        record.delete(getActivity());
                        ((MainActivity) getActivity()).showMainFragment();
                    }
                });

                builder.setNeutralButton(android.R.string.cancel,null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();;
                }
            });
        return rootView;
    }
}
