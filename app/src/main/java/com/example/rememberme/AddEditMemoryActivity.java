package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rememberme.DB.MemoriesDbSource;

import java.util.ArrayList;

public class AddEditMemoryActivity extends AppCompatActivity implements View.OnClickListener{

    public static Memory memory;
    MemoriesDbSource dbSource;
    long id;

    EditText title;
    EditText text;
    ImageView image;
    Button audio;
    Button saveMemory;
    Button cancelMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        dbSource = new MemoriesDbSource(this);
        dbSource.open();

        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        image = findViewById(R.id.memory_image);
        audio = findViewById(R.id.add_audio);
        saveMemory = findViewById(R.id.save_memory);
        cancelMemory = findViewById(R.id.cancel_memory);
        audio.setOnClickListener(this);
        saveMemory.setOnClickListener(this);
        cancelMemory.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getIntExtra(ViewMemory.ID_KEY, -1);
        if (id < 0) {
            memory = new Memory();
        }
        else {
            Log.d("rdudak", id + "");
            memory = dbSource.fetchEntryByIndex(id);
            loadData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_audio:
                //TODO record audio
                break;

            case R.id.save_memory:
                saveMemoryData();
                dbSource.close();
                break;

            case R.id.cancel_memory:
                dbSource.close();
                finish();
                break;
        }
    }

    public void saveMemoryData() {
        memory.setTitle(title.getText().toString());
        memory.setText(text.getText().toString());
       // memory.setImage();
        if (id >= 0) {
            dbSource.updateEntry(id);
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            dbSource.insertMemory(memory);
            Toast.makeText(this, "New Framily Member Saved", Toast.LENGTH_SHORT).show();
            id = dbSource.fetchLastEntry().getId();
        }
    }

    public void loadData() {
        title.setText(memory.getText());
        text.setText(memory.getText());
      //  image.setImageURI();
    }
}