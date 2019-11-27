package bwa.example.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TicketSuccessAct extends AppCompatActivity {

    Button btn_my_dashboard, btn_view_tickets;
    Animation app_splash, btt, ttb;
    TextView app_title, app_subtitle;
    ImageView icon_success_ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_success);

        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);
        btn_view_tickets = findViewById(R.id.btn_view_tickets);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);
        icon_success_ticket = findViewById(R.id.icon_success_ticket);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        //run animation
        icon_success_ticket.startAnimation(app_splash);
        app_title.startAnimation(ttb);
        app_subtitle.startAnimation(ttb);
        btn_view_tickets.startAnimation(btt);
        btn_my_dashboard.startAnimation(btt);

        btn_view_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(TicketSuccessAct.this, MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(TicketSuccessAct.this, HomeAct.class);
                startActivity(backtohome);
            }
        });

    }
}
