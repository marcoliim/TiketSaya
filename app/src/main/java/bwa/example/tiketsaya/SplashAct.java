package bwa.example.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {

    Animation app_splash, btt;
    ImageView app_logo;
    TextView app_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_act);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        //load element
        app_logo = findViewById(R.id.app_logo);
        app_subtitle = findViewById(R.id.app_subtitle);

        //run Animation
        app_logo.startAnimation(app_splash);
        app_subtitle.startAnimation(btt);

        //Set timer for 2 second
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Change activity to another activity
                Intent gogetstarted = new Intent(SplashAct.this,GetStarted.class);
                startActivity(gogetstarted);
                finish();
            }
        },2000);
    }
}
