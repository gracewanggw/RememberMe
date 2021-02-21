package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddEditMemoryActivity extends AppCompatActivity {

    public static Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);
    }

}