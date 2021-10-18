package com.example.notefy;

import com.google.firebase.database.Exclude;

public class Note {
    private String title;
    private String body;
    private String key;

    public Note(){
        //Empty Constructor needed
    }

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
