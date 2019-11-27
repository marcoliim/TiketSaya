package bwa.example.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {


    Button btn_continue;
    LinearLayout btn_back;
    EditText username, password, email_address;
    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        btn_continue = findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loading state
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading...");

                final String xusername = username.getText().toString();
                final String xpassword = password.getText().toString();
                final String xemail_address = email_address.getText().toString();

                if (xusername.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill an Username!", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }
                else {
                    if (xpassword.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please fill a Password!", Toast.LENGTH_SHORT).show();
                        btn_continue.setEnabled(true);
                        btn_continue.setText("CONTINUE");
                    }
                    else {
                        if (xemail_address.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Please fill an Email Address!", Toast.LENGTH_SHORT).show();
                            btn_continue.setEnabled(true);
                            btn_continue.setText("CONTINUE");
                        }
                        else {
                            //save data to local storage(phone storage)
                            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(username_key, username.getText().toString());
                            editor.apply();

                            //Save to Database
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                    dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                    dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                    dataSnapshot.getRef().child("user_balance").setValue(800);

                                    Intent gotonextregister = new Intent(RegisterOneAct.this,RegisterTwoAct.class);
                                    startActivity(gotonextregister);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtosignin = new Intent(RegisterOneAct.this,SignInAct.class);
                startActivity(backtosignin);
            }
        });

    }
}
