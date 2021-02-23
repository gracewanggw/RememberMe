package com.example.rememberme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rememberme.DB.RememberMeDbSource;

public class AddEditMemoryActivity extends AppCompatActivity implements View.OnClickListener{

    public static Memory memory;
    public static Framily framily;
    RememberMeDbSource dbSource;
    long framilyId;
    long memoryId;

    EditText title;
    EditText text;
    ImageView image;
    Button audio;
    Button saveMemory;
    Button cancelMemory;
    Button removeMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        image = findViewById(R.id.memory_image);
        audio = findViewById(R.id.add_audio);
        saveMemory = findViewById(R.id.save_memory);
        cancelMemory = findViewById(R.id.cancel_memory);
        removeMemory = findViewById(R.id.remove);
        audio.setOnClickListener(this);
        saveMemory.setOnClickListener(this);
        cancelMemory.setOnClickListener(this);
        removeMemory.setOnClickListener(this);

        Intent intent = getIntent();
        framilyId = intent.getLongExtra(FramilyProfile.ID_KEY, -1);
        Log.d("rdudak", "id = " + framilyId);
        framily = dbSource.fetchFramilyByIndex(framilyId);
        memoryId = intent.getIntExtra(ViewMemory.ID_MEMORY, -1);
        if (memoryId < 0) {
            memory = new Memory();
            removeMemory.setVisibility(View.GONE);
        }
        else {
            Log.d("rdudak", memoryId + "");
            memory = dbSource.fetchMemoryByIndex(memoryId);
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
                finish();
                break;

            case R.id.cancel_memory:
                dbSource.close();
                finish();
                break;

            case R.id.remove:
                askRemove(this);
                break;
        }
    }

    public void saveMemoryData() {
        memory.setTitle(title.getText().toString());
        memory.setText(text.getText().toString());
       // memory.setImage();
        if (memoryId >= 0) {
            dbSource.updateMemoryEntry(memoryId);
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            dbSource.insertMemory(memory);
            Toast.makeText(this, "New Framily Member Saved", Toast.LENGTH_SHORT).show();
            memoryId = dbSource.fetchLastMemoryEntry().getId();
            framily.addMemory(memoryId);
            dbSource.updateFramilyEntry(framilyId, framily);
            Log.d("rdudak", "updated framily id: " + framilyId + ", memories: " + framily.getMemories().toString());

        }
    }

    public void loadData() {
        title.setText(memory.getText());
        text.setText(memory.getText());
      //  image.setImageURI();
    }

    public void askRemove(Context context) {
        final String field = "Are you sure you want to remove this memory?";
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(field)
                .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbSource.removeMemory(memoryId);
                        finish();
                    }
                })
                .setNegativeButton("No, go back!", null)
                .create();
        dialog.show();
    }
}