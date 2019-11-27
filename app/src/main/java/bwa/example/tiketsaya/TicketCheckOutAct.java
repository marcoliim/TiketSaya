package bwa.example.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TicketCheckOutAct extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_pay_now, btnmines, btnplus;
    TextView textjumlahticket, textmybalance, texttotalharga, nama_wisata, lokasi, ketentuan;
    ImageView notice_uang;
    Integer valuejumlahticket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    Integer sisa_balance = 0;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    //generate nomor transaksi secara random
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_check_out);

        getUsernameLocal();

        //get data from intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_ticket_baru = bundle.getString("jenis_tiket");

        btn_pay_now = findViewById(R.id.btn_pay_now);
        btnmines = findViewById(R.id.btnmines);
        btnplus = findViewById(R.id.btnplus);
        btn_back = findViewById(R.id.btn_back);

        textjumlahticket = findViewById(R.id.textjumlahtiket);
        textmybalance = findViewById(R.id.textmybalance);
        texttotalharga = findViewById(R.id.texttotalharga);
        notice_uang = findViewById(R.id.notice_uang);

        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);

        //set a new value setting
        textjumlahticket.setText(valuejumlahticket.toString());


        //hide mines btn as default
        btnmines.animate().alpha(0).setDuration(300).start();
        btnmines.setEnabled(false);
        notice_uang.setVisibility(View.GONE);

        //take active user data from firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmybalance.setText("$ " + mybalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //take data from database based on intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_ticket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //overwrite the existing data with the new data
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();
                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                valuetotalharga = valuehargatiket * valuejumlahticket;
                texttotalharga.setText("$ " + valuetotalharga+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahticket += 1;
                textjumlahticket.setText(valuejumlahticket.toString());
                if (valuejumlahticket > 1) {
                    btnmines.animate().alpha(1).setDuration(300).start();
                    btnmines.setEnabled(true);
                }
                valuetotalharga = valuehargatiket * valuejumlahticket;
                texttotalharga.setText("$ " + valuetotalharga+"");
                if (valuetotalharga > mybalance){
                    btn_pay_now.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_pay_now.setEnabled(false);
                    textmybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });

        btnmines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahticket -= 1;
                textjumlahticket.setText(valuejumlahticket.toString());
                if (valuejumlahticket < 2) {
                    btnmines.animate().alpha(0).setDuration(300).start();
                    btnmines.setEnabled(false);
                }
                valuetotalharga = valuehargatiket * valuejumlahticket;
                texttotalharga.setText("$ " + valuetotalharga);
                if (valuetotalharga < mybalance){
                    btn_pay_now.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay_now.setEnabled(true);
                    textmybalance.setTextColor((Color.parseColor("#203DD1")));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save user data and create My Tickets table
                reference3 = FirebaseDatabase.getInstance().getReference().child("My Tickets").child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_ticket").setValue(valuejumlahticket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent gototicketsuccess = new Intent(TicketCheckOutAct.this, TicketSuccessAct.class);
                        startActivity(gototicketsuccess);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update balance to active users
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = mybalance - valuetotalharga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoticketdetail = new Intent(TicketCheckOutAct.this,TicketDetailAct.class);
                startActivity(backtoticketdetail);
            }
        });

    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
