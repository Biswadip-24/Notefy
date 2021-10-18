package com.example.notefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNote extends AppCompatActivity
{
    private TextView mTitle;
    private TextView mBody;
    private TextView mSave;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mTitle = findViewById(R.id.AddNote_textview_title);
        mBody = findViewById(R.id.AddNote_textview_body);

        mBody.setMovementMethod(LinkMovementMethod.getInstance());
        mSave = findViewById(R.id.AddNote_save);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dbref = FirebaseDatabase.getInstance().getReference(user.getUid());

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note(mTitle.getText().toString(), mBody.getText().toString());
                String uploadID = dbref.push().getKey();
                dbref.child(uploadID).setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNote.this, "Note Successfully Added", Toast.LENGTH_SHORT).show();
                        closeActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeActivity(){
        this.finish();
    }
}