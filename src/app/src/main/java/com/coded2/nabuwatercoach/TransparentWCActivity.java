package com.coded2.nabuwatercoach;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rogerioso on 01/06/2015.
 */
public class TransparentWCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.ativity_splash);
        AddWaterFragment dialog = new AddWaterFragment();
        dialog.show(getSupportFragmentManager(), null);

    }
}
