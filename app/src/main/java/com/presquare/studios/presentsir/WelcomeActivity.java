package com.presquare.studios.presentsir;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.github.paolorotolo.appintro.AppIntro;


public class WelcomeActivity extends AppIntro {

    private PrefManager prefManager;


    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide1));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide2));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide3));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide4));

        hideStatusBar();
        setFlowAnimation();

    }



    private void hideStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

    }


    @Override
    public void onSkipPressed() {

        Intent skip = new Intent(this, LoginActivity.class);

        finish();

        startActivity(skip);


    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {

        Intent done = new Intent(this, LoginActivity.class);

        finish();

        startActivity(done);

    }

    @Override
    public void onSlideChanged() {

    }

    public void getStarted(View v) {
        Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }



}

