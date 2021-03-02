package com.example.rememberme;

import android.graphics.Bitmap;

import java.io.File;

public class Memory {
    Long id;
    String title;
    String text;
    Bitmap image;
    String audio;

    public void setId(Long id){this.id = id;}
    public Long getId() {return id;}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setText(String text) {this.text = text;}
    public String getText() {return text;}

    public void setImage(Bitmap image) {this.image = image;}
    public Bitmap getImage() {return image;}

    public void setAudio(String audio) {this.audio = audio;}
    public String getAudio() {return audio;}

}

