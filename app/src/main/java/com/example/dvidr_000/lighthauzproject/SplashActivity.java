package com.example.dvidr_000.lighthauzproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.test.InstrumentationTestRunner;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread myThread = new Thread()
        {
          public void run()
          {
              try
              {
                  sleep(3000);
                  Intent startMainScreen = new Intent(getApplicationContext(),LoginActivity.class);
                  startActivity(startMainScreen);
                  finish();


              } catch (InterruptedException e)
              {
               e.printStackTrace();
              }
          }
        };
    myThread.start();
    }
}
