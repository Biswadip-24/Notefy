package com.example.notefy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNote extends AppCompatActivity
{
    private TextView mtitle;
    private TextView mBody;
    private TextView mSave;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mtitle = findViewById(R.id.EditNote_textview_title);
        mBody = findViewById(R.id.EditNote_textview_body);
        mSave = findViewById(R.id.Edit_save);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dbref = FirebaseDatabase.getInstance().getReference(user.getUid());

        mtitle.setText(getIntent().getStringExtra("title"));
        mBody.setText(getIntent().getStringExtra("body"));
        mBody.setMovementMethod(LinkMovementMethod.getInstance());
        String key = getIntent().getStringExtra("key");


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = mtitle.getText().toString();
                String newBody = mBody.getText().toString();

                Note note = new Note(newTitle,newBody);
                note.setKey(key);
                dbref.child(key).setValue(note);
                closeActivity();

            }
        });
    }

    private void closeActivity(){
        this.finish();
    }
}