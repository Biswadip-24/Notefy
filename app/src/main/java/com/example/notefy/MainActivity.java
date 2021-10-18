package com.example.notefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener
{
    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private List<Note> mNoteList;
    private int NUM_COLUMNS = 2;
    private FloatingActionButton fab;
    private ProgressBar mProgressBar;
    private TextView mNoNotes;

    private DatabaseReference dbref;
    private ValueEventListener mListener;
    private EditText msearchText;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialisation();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNoteActivity();
            }
        });

        mNoteList = new ArrayList<>();

        mNoteAdapter = new NoteAdapter(this , mNoteList);
        mRecyclerView.setAdapter(mNoteAdapter);
        mNoteAdapter.setOnItemClickListener(MainActivity.this);


        dbref = FirebaseDatabase.getInstance().getReference(user.getUid());

        mListener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mNoteList != null){
                    mNoteList.clear();
                }

                for(DataSnapshot noteSnapshot : snapshot.getChildren()){
                    Note note = noteSnapshot.getValue(Note.class);
                    note.setKey(noteSnapshot.getKey());
                    mNoteList.add(note);
                }
                mNoteAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);

                if(mNoteList.isEmpty()) mNoNotes.setVisibility(View.VISIBLE);
                else mNoNotes.setVisibility(View.INVISIBLE);

                filter(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mProgressBar.setVisibility(View.INVISIBLE);

                if(mNoteList.isEmpty()) mNoNotes.setVisibility(View.VISIBLE);
                else mNoNotes.setVisibility(View.INVISIBLE);

                Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        msearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                text = editable.toString();
                filter(text);
            }
        });
    }

    private void filter(String text)
    {
        ArrayList<Note> filteredList = new ArrayList<>();
        if(text.trim().equals("")){
            filteredList = new ArrayList<>(mNoteList);
            mNoteAdapter.filterList(filteredList);
            return;
        }
        for (Note item : mNoteList) {
            if (item.getBody().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                filteredList.add(item);
            }
            else if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        mNoteAdapter.filterList(filteredList);
    }

    @Override
    public void onItemClick(Note note) {
        //Toast.makeText(this , "Item Click at position : "+position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this , EditNote.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("body",note.getBody());
        intent.putExtra("key",note.getKey());

        startActivity(intent);
    }

    @Override
    public void onDeleteClick(String key) {
        dbref.child(key).removeValue();
        //Toast.makeText(this , "Delete Click at position : "+position,Toast.LENGTH_SHORT).show();
    }

    private void openAddNoteActivity(){
        Intent intent = new Intent(this , AddNote.class);
        startActivity(intent);
    }

    private void initialisation()
    {
        fab = findViewById(R.id.Add_note_button);
        mProgressBar = findViewById(R.id.progress_bar);
        mNoNotes = findViewById(R.id.NoNote_textView);
        msearchText = findViewById(R.id.search_text);
        mRecyclerView = findViewById(R.id.recycler_view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbref.removeEventListener(mListener);
    }
}