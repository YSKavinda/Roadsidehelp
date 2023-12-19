package com.codevibe.roadsidehelp_client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.squareup.picasso.Picasso;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RoadSideHelpClient_Fullscreen);
        setContentView(R.layout.activity_splash);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        ImageView imageView = findViewById(R.id.splashLogo);
        Picasso.get().load(R.drawable.rdh).resize(400, 300).into(imageView);

        SpringAnimation springAnimation = new SpringAnimation(imageView, SpringAnimation.TRANSLATION_X, 0);
        springAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnimation.start();


        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                    }
                }
                , 1000);


        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

//                        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();


                        findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                , 4000);

    }
}