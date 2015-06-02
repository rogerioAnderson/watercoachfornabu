package com.coded2.nabuwatercoach;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rogerioso on 01/06/2015.
 */
public class TransparentWCActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_splash);

        AddWaterFragment dialog = new AddWaterFragment();

        dialog.show(getFragmentManager(), null);

    }
}
