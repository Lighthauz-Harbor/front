package com.example.dvidr_000.lighthauzproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager;
        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String,String> user = sessionManager.getUserDetails();

        if (user.get(SessionManager.KEY_TOKEN).isEmpty()){
            Intent startMainScreen = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(startMainScreen);
            finish();
        }
        else {
            Intent in = new Intent(SplashActivity.this,HomeActivity.class);
            startActivity(in);
            finish();
        }


    }
}
