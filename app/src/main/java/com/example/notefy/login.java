package com.example.notefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class login extends AppCompatActivity
{
    private Button mlogin;
    private TextView mforgot;
    private Button mNewUser;
    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mlogin = findViewById(R.id.Button_login);
        mforgot = findViewById(R.id.TextView_forgot);
        mNewUser = findViewById(R.id.new_user);
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(login.this,"All Fields are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Login user to Firebase
                    mAuth.signInWithEmailAndPassword(mail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        checkmailVerification();
                                    }
                                    else{
                                        Toast.makeText(login.this, "Make Sure email and password is correct", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

        mforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,Forgot_password.class));
                //login.this.finish();
            }
        });

        mNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,signup.class));
                //login.this.finish();
            }
        });
    }

    private void checkmailVerification(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()){
            Toast.makeText(login.this, "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(login.this,MainActivity.class));
        }
        else{
            Toast.makeText(login.this, "Verify your mail first", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}