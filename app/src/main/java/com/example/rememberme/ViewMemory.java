package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewMemory extends AppCompatActivity {

    public static final String ID_MEMORY = "id_memory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);
    }
}