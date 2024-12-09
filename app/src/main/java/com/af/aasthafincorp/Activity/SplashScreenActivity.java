package com.af.aasthafincorp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.af.aasthafincorp.MainActivity;
import com.af.aasthafincorp.R;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;
import com.af.aasthafincorp.Utility.UtilityClass;

public class SplashScreenActivity extends AppCompatActivity {

    UtilityClass utilityClass;
    String unid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        utilityClass = new UtilityClass(SplashScreenActivity.this);


        if (SaveSharedPreferences.getUniqueID(this).length() == 0) {
            SaveSharedPreferences.setUniqueID(this, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        }

        if(unid==null){
            unid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }


        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                CallNextScreen();

            }
        };
        handler.postDelayed(r, 3000);
    }

    private void CallNextScreen() {

        Log.d("TAG","UserName="+SaveSharedPreferences.getUname(this));

            if (SaveSharedPreferences.getUname(SplashScreenActivity.this).isEmpty()) {

                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else {
               // Intent intent = new Intent(SplashScreenActivity.this, WebdemoActivity.class);
                //Intent intent = new Intent(SplashScreenActivity.this, WebActivity.class);
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
    }
}