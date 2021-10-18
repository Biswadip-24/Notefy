package com.example.notefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class signup extends AppCompatActivity
{
    private Button mSignUp;
    private TextView mLogin;
    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignUp = findViewById(R.id.Button_signup);
        mLogin = findViewById(R.id.want_to_login);
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);

        mAuth = FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.this.finish();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(signup.this,"All Fields are required",Toast.LENGTH_SHORT).show();
                }
                else if(password.length() <= 7){
                    Toast.makeText(signup.this,"Length of password should be atleast 8 characters",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Register user to Firebase
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Notefy", "createUserWithEmail:success");
                                        Toast.makeText(signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        sendEmailVerification();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d("Notefy", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    private void sendEmailVerification(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(signup.this, "Email Verification Sent, Verify and login", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            finish();

                        }
                    });
        }
        else{
            Toast.makeText(signup.this, "Failed to Send Verification Email", Toast.LENGTH_LONG).show();
        }

    }

}