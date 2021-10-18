package com.example.notefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forgot_password extends AppCompatActivity
{
    private Button mRecover;
    private TextView mLogin;
    private EditText mEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mLogin = findViewById(R.id.back_to_login);
        mEmail = findViewById(R.id.forgot_email);

        mRecover = findViewById(R.id.recover);

        mAuth = FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Forgot_password.this.finish();
            }
        });

        mRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mEmail.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(Forgot_password.this,"Enter your Email",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Recover
                    mAuth.sendPasswordResetEmail(mail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Forgot_password.this, "Password Recovery Mail Sent", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Forgot_password.this, "task.getException()", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}