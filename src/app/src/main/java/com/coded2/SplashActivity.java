package com.coded2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.coded2.nabuwatercoach.R;
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.exception.NabuUtilityMissingException;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.interfaces.UserProfileListener;
import com.razer.android.nabuopensdk.models.Scope;
import com.razer.android.nabuopensdk.models.UserProfile;

/**
 * Created by rogerioso on 26/05/2015.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ativity_splash);
        TextView versionTXT = (TextView) findViewById(R.id.splash_txt_version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            versionTXT.setText("version "+pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionTXT.setText("");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    intialize();
                }catch(Exception e){
                    Log.w(Constants.APPLICATION_TAG,e.getMessage());
                }


            }
        },3000);

    }

    private void intialize() throws NabuUtilityMissingException {

        DialogManager.showLoading(SplashActivity.this, true);
        boolean firstRun = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean(getString(R.string.pref_key_first_run),true);
        if(firstRun){

            DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initNabu();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishSplash();
                }
            };

            DialogManager.confirm(this, R.string.login_razerid_title, R.string.login_razerid_message, android.R.string.cancel, android.R.string.ok,
                    confirmListener, cancelListener);


        }else{
            finishSplash();
        }
    }

    private void initNabu() {
        final NabuOpenSDK nabuSdk = NabuOpenSDK.getInstance(this);

        nabuSdk.initiate(SplashActivity.this, getResources().getString(R.string.razer_app_id), new String[]{Scope.COMPLETE}, new NabuAuthListener() {
            @Override
            public void onAuthSuccess(String s) {
                nabuSdk.getUserProfile(SplashActivity.this, new UserProfileListener() {
                    @Override
                    public void onReceiveData(UserProfile userProfile) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit();
                        editor.putString(getString(R.string.pref_key_nickname), userProfile.nickName);
                        editor.apply();
                        finishSplash();
                    }

                    @Override
                    public void onReceiveFailed(String s) {
                        Toast.makeText(SplashActivity.this, R.string.get_userProfile_fail + "\n" + s, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onAuthFailed(String s) {
                Toast.makeText(SplashActivity.this, s, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    private void finishSplash() {
        Intent it = new Intent(SplashActivity.this, MainActivity.class);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit();
        editor.putBoolean(getString(R.string.pref_key_first_run), false).apply();
        startActivity(it);
        finish();
    }
}
