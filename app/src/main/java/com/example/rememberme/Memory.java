package com.example.rememberme;

import android.graphics.Bitmap;

public class Memory {
    Long id;
    String title;
    String text;
    String image; // String is file path
    //audio file?

    public void setId(Long id){this.id = id;}
    public Long getId() {return id;}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setText(String text) {this.text = text;}
    public String getText() {return text;}

    public void setImage(String image) {this.image = image;}
    public String getImage() {return image;}

}

