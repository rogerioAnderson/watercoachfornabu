package com.coded2.nabuwatercoach;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.coded2.DialogManager;
import com.razer.android.nabuopensdk.NabuOpenSDK;
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
                intialize();
            }
        },3000);

    }

    private void intialize() {
        final NabuOpenSDK nabuSdk = NabuOpenSDK.getInstance(SplashActivity.this);
        DialogManager.showLoading(SplashActivity.this, true);
        boolean firstRun = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean(getString(R.string.pref_key_first_run),true);
        if(firstRun){
            nabuSdk.initiate(SplashActivity.this, getResources().getString(R.string.razer_app_id), new String[]{Scope.COMPLETE}, new NabuAuthListener() {
                @Override
                public void onAuthSuccess(String s) {

                    nabuSdk.getUserProfile(SplashActivity.this, new UserProfileListener() {
                        @Override
                        public void onReceiveData(UserProfile userProfile) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit();
                            editor.putBoolean(getString(R.string.pref_key_first_run), false);
                            editor.putString(getString(R.string.pref_key_nickname), userProfile.nickName);
                            editor.apply();
                            Intent it = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(it);
                            finish();
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
                    Toast.makeText(SplashActivity.this,s,Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }else{
            Intent it = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }
    }
}
