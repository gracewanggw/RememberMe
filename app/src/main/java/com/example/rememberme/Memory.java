package com.example.rememberme;

import android.graphics.Bitmap;

import java.io.File;

public class Memory {
    Long id;
    String title;
    String text;
    byte[] image;
    String audio;
    String filename;

    public void setId(Long id){this.id = id;}
    public Long getId() {return id;}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setText(String text) {this.text = text;}
    public String getText() {return text;}

    public void setImage(byte[] image) {this.image = image;}
    public byte[] getImage() {return image;}

    public void setAudio(String audio) {this.audio = audio;}
    public String getAudio() {return audio;}

    public void setFilename(String filename){this.filename = filename;}
    public String getFilename(){return filename;}

}

