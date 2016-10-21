package com.presquare.studios.presentsir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class Splash extends AppCompatActivity {

    private PrefManager prefManager;

    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstruncheck();

    }


    private void firstruncheck() {

        prefManager = new PrefManager(this);

        if (!prefManager.isFirstTimeLaunch()) {

            Intent go = new Intent(this, LoginActivity.class);

            finish();

            startActivity(go);

        }

        else {

            Intent go = new Intent(this, WelcomeActivity.class);

            finish();

            startActivity(go);

            prefManager.unsetFirstTimeLaunch(false);

        }

    }

}