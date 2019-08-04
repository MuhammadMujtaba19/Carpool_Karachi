package com.example.carpoolkarachi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private com.google.android.material.textfield.TextInputEditText username;
    private com.google.android.material.textfield.TextInputEditText email;
    private com.google.android.material.textfield.TextInputEditText password;
    private com.google.android.material.textfield.TextInputEditText phone;
    private RadioGroup sexRadio;
    private RadioButton sexButton;
    private ProgressDialog progressDialog;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        registerUser();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
////        updateUI
////        startActivity(new Intent(Signup_Activity.this, Signin_Activity.class));
//
//    }

    //    public void registerUser(){
//
//    }
//
    public void registerUser() {
        Button register = (Button) findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.useremail);
        password = findViewById(R.id.userpassword);
        phone = findViewById(R.id.userphone);
        sexRadio = findViewById(R.id.gender);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String gen = (gender.getCheckedRadioButtonId());

                Log.i("Username", username.getText().toString());
                Log.i("Username", email.getText().toString());
                Log.i("Username", phone.getText().toString());
                Log.i("Username", password.getText().toString());
//                Log.i("Username", String.valueOf(gender.getCheckedRadioButtonId()));
//                System.out.println((gender.getCheckedRadioButtonId()));
                createUser();
//                int genderId = sexRadio.getCheckedRadioButtonId();
//                sexButton = findViewById(genderId);
//                Log.i("Sex",sexButton.getText().toString());
//                storeUserInfo();
            }
        });
    }

    public void createUser(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("User Registered!", "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            storeUserInfo(user);
                            startActivity(new Intent(Signup_Activity.this, Signin_Activity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Authentication Failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                                    updateUI();
                        }
                    }
                });
    }

    public void storeUserInfo(FirebaseUser user){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = mAuth.getCurrentUser();
        int genderId = sexRadio.getCheckedRadioButtonId();
        sexButton = findViewById(genderId);
        UserInfo userInfo = new UserInfo(username.getText().toString(), phone.getText().toString(), sexButton.getText().toString());
        databaseReference.child(user.getUid()).setValue(userInfo);
    }
}
