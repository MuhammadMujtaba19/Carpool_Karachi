package com.example.carpoolkarachi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
//        configGotoSignupButton();

        TextView signup = (TextView) findViewById(R.id.signuptext);
        signup.setOnClickListener(this);

        Button login = findViewById(R.id.viewDetails);
        login.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Go to home page
//        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signuptext:
                startActivity(new Intent(Signin_Activity.this, Signup_Activity.class));
                break;
            case R.id.viewDetails:
                loginUser();
                break;

        }
    }

    public void loginUser() {
        EditText emailEditText = findViewById(R.id.useremail);
        EditText passwordEditText = findViewById(R.id.userpassword);
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Signin_Activity.this, "Please enter email.",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(Signin_Activity.this, "Please enter password.",
                    Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Signing In");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Signed In", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(Signin_Activity.this, "Signed In",
                                        Toast.LENGTH_SHORT).show();
//                            updateUI(user);

                                // User Signed in from here!
                                Intent i = new Intent(Signin_Activity.this,MainActivity.class);
                                startActivity(i);
                                finish();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Auth Failed", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Signin_Activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

}
