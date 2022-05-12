package com.example.plant_ev10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    //variabili
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, name, pwred;
    public static MediaPlayer mp;
    private static final int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        //init music
        mp = MediaPlayer.create(this,R.raw.cushy);
        mp.setLooping(true);
        mp.setVolume(50,50);
        mp.start();
        //animazioni
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.logo);
        logo = findViewById(R.id.plante);
        name = findViewById(R.id.armandosantoro);
        pwred = findViewById(R.id.pwrdby);

        image.setAnimation(topAnim);
        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        pwred.setAnimation(bottomAnim);


        new Handler() .postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomeActivity.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);


    }

    private void MainActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
    }
    public void startMusic(View v)
    {
        mp.start();
    }

    public void stopMusic(View v)
    {
        mp.stop();
    }
   // @Override
  /*  protected void onPause()
    {
        super.onPause();
        mp.stop();
    }*/
    @Override
    protected void onResume()
    {
        super.onResume();
        mp.start();
    }
    /*@Override
    protected void onStop() {
        super.onStop();
        mp.stop();
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
    }
}