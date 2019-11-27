package bwa.example.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class SignInAct extends AppCompatActivity {

    TextView btn_new_account;
    Button btn_sign_in;
    EditText xusername, xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loading state
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an Username!", Toast.LENGTH_SHORT).show();
                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("SIGN IN");
                }

                else {
                   if (password.isEmpty()){
                       Toast.makeText(getApplicationContext(), "Please enter a Password!", Toast.LENGTH_SHORT).show();
                       btn_sign_in.setEnabled(true);
                       btn_sign_in.setText("SIGN IN");
                   }
                   else {
                       reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                       reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()){

                                   //get password data from Firebase
                                   String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                   //password validation
                                   if (password.equals(passwordFromFirebase)){

                                       SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                       editor.putString(username_key, xusername.getText().toString());
                                       editor.apply();

                                       Intent gotohome = new Intent(SignInAct.this,HomeAct.class);
                                       startActivity(gotohome);
                                   }

                                   else {
                                       Toast.makeText(getApplicationContext(),"Incorrect Password!", Toast.LENGTH_SHORT).show();
                                       //loading state
                                       btn_sign_in.setEnabled(true);
                                       btn_sign_in.setText("SIGN IN");
                                   }
                               }
                               else {
                                   Toast.makeText(getApplicationContext(),"Username not found!", Toast.LENGTH_SHORT).show();
                                   //loading state
                                   btn_sign_in.setEnabled(true);
                                   btn_sign_in.setText("SIGN IN");
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                               Toast.makeText(getApplicationContext(), "Database ERROR!", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
                }
            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(SignInAct.this,RegisterOneAct.class);
                startActivity(gotoregisterone);
            }
        });
    }
}
