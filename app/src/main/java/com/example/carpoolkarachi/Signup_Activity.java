package com.example.carpoolkarachi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Activity extends AppCompatActivity implements  View.OnClickListener{
    private FirebaseAuth mAuth;
    private com.google.android.material.textfield.TextInputEditText usernameEdittext;
    private com.google.android.material.textfield.TextInputEditText emailEdittext;
    private com.google.android.material.textfield.TextInputEditText passwordEdittext;
    private com.google.android.material.textfield.TextInputEditText phoneEdittext;
    private RadioGroup sexRadio;
    private RadioButton sexButton;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String sex;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private Button register;
    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
//        registerUser();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) then go to signin page.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null) {
//            startActivity(new Intent(Signup_Activity.this, Signin_Activity.class));
//        }
//    }

    public boolean getInfo(){
        usernameEdittext = findViewById(R.id.username);
        emailEdittext = findViewById(R.id.useremail);
        passwordEdittext = findViewById(R.id.userpassword);
        phoneEdittext = findViewById(R.id.userphone);
        sexRadio = findViewById(R.id.gender);
        username = usernameEdittext.getText().toString().trim();
        email = emailEdittext.getText().toString().trim();
        password = passwordEdittext.getText().toString().trim();
        phone = phoneEdittext.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Signup_Activity.this, "Please enter email.",
                    Toast.LENGTH_SHORT).show();
            return  false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(Signup_Activity.this, "Please enter password.",
                    Toast.LENGTH_SHORT).show();
            return  false;
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Signup_Activity.this, "Please enter phone.",
                    Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;

    }

    public void createUser(){
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
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
        sex = sexButton.getText().toString();
        UserInfo userInfo = new UserInfo(username, phone, sex);
        databaseReference.child(user.getUid()).setValue(userInfo);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.register:
                if (getInfo()){
                    createUser();
                }
                break;
            case R.id.signIn:
                startActivity(new Intent(Signup_Activity.this, Signin_Activity.class));
        }
    }
}
