package com.example.notefy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends AppCompatActivity
{
    private int duration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeInt=new Intent(Welcome.this,login.class);
                startActivity(homeInt);
                finish();
            }
        },duration);
    }
}