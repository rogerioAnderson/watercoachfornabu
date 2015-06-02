package com.coded2.nabuwatercoach;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.coded2.Constants;
import com.coded2.MainActivity;

public class AddWaterFragment extends DialogFragment {

    SeekBar addWaterBar;
    EditText valueml;
    int mlValue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_water, null);

        addWaterBar = (SeekBar) view.findViewById(R.id.add_water_bar);
        valueml = (EditText) view.findViewById(R.id.mlvalue);
        mlValue=0;
        valueml.setText(Integer.toString(mlValue));

        addWaterBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mlValue = progress * 10;
                valueml.setText(Integer.toString(mlValue));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setTitle(R.string.label_add_water);

        builder.setView(view).
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mlValue > 0) {
                            DailyRecord record = new DailyRecord();
                            record.ml = mlValue;
                            record.save(getActivity());
                            close();
                        }

                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(Constants.WATER_COACH_NOTIFICATION_ID);

                    }
                });

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    close();
                }
                return false;
            }
        });
        return builder.create();
    }

    private void close() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).showMainWaterCoachFragment();
        } else {
            activity.finish();
        }
    }
}