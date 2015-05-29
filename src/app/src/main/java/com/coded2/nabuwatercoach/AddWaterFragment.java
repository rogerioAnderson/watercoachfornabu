package com.coded2.nabuwatercoach;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

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
                            record.ml= mlValue;
                            record.save(getActivity());
                            ((MainActivity) getActivity()).showMainFragment();
                        }
                    }
                });

        return builder.create();
    }
}