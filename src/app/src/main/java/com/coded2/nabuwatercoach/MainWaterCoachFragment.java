package com.coded2.nabuwatercoach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coded2.DBHelper;
import com.coded2.MainActivity;
import com.coded2.Util;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainWaterCoachFragment extends Fragment {

    TextView score ;
    TextView labelDate;
    ProgressBar goalBar;
    GridView dailyRecords;
    FloatingActionButton addButon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_water_coach_main, container, false);


        score = (TextView) rootView.findViewById(R.id.lbl_score);
        goalBar = (ProgressBar) rootView.findViewById(R.id.goal_bar);
        dailyRecords = (GridView) rootView.findViewById(R.id.daily_records);
        addButon = (FloatingActionButton) rootView.findViewById(R.id.add_water_btn);
        goalBar.setProgressDrawable(getResources().getDrawable(R.drawable.razer_progress));
        labelDate = (TextView) rootView.findViewById(R.id.lbl_date);

        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWaterFragment dialog = new AddWaterFragment();
                dialog.show(getFragmentManager(), null);
            }
        });


        updateProgress(null);


        return rootView;
    }

    private void updateProgress(Date date) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String goal = prefs.getString(getResources().getString(R.string.pref_key_goal),getString(R.string.default_water_ml_goal));
        goalBar.setMax(Integer.parseInt(goal));

        int progress = 0;
        DBHelper helper = new DBHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        List<WaterDailyRecord> records = WaterDailyRecord.listOFDay(getActivity(),date);

        for (WaterDailyRecord record: records){
            progress+=record.ml;
        }

        score.setText(progress + "/" + goal + " ml");

        goalBar.setProgress(progress);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getString(R.string.pref_key_current_score),progress);
        editor.commit();

        WaterCoachRegistryAdapter adapter = new WaterCoachRegistryAdapter(getActivity(),R.layout.registry, records);
        dailyRecords.setAdapter(adapter);




        if(date==null || Util.isToday(date)){
            dailyRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.remove_water_dialog_title);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WaterDailyRecord record = (WaterDailyRecord) dailyRecords.getAdapter().getItem(position);
                            record.delete(getActivity());
                            ((MainActivity) getActivity()).showMainWaterCoachFragment();
                        }
                    });

                    builder.setNeutralButton(android.R.string.cancel, null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            Date now = new Date(System.currentTimeMillis());
            labelDate.setText(Util.formatDateLocale(now));

        }else{
            labelDate.setText(Util.formatDateLocale(date));
            dailyRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Toast.makeText(getActivity(),R.string.imutable_past_information,Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_water, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_calendar){

            FragmentManager fm = getFragmentManager();
            Calendar now = Calendar.getInstance();

            CalendarDatePickerDialog  calendarDatePickerDialog = CalendarDatePickerDialog
                    .newInstance(new CalendarDatePickerDialog.OnDateSetListener() {
                                     @Override
                                     public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int month, int day) {
                                         Calendar calendar = Calendar.getInstance();
                                         calendar.set(year,month,day,0,0,0);
                                         calendar.set(Calendar.MILLISECOND,0);
                                         MainWaterCoachFragment.this.updateProgress(calendar.getTime());
                                     }
                                 }, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                            now.get(Calendar.DATE));
            calendarDatePickerDialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.Base_Theme_AppCompat_Dialog);
            calendarDatePickerDialog.show(fm, "date_picker");
        }
        return super.onOptionsItemSelected(item);
    }



}
