package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.DB.RememberMeDbSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ViewMemory extends AppCompatActivity implements View.OnClickListener {

    public static final String ID_MEMORY = "id_memory";

    RememberMeDbSource dbSource;

    Button back;
    Button edit;
    Button speaker;
    TextView title;
    TextView text;
    ImageView image;

    long framilyId;
    long memoryId;

    Framily framily;
    Memory memory;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        back = findViewById(R.id.backToPerson);
        back.setOnClickListener(this);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(this);
        title = findViewById(R.id.title_memory);
        text = findViewById(R.id.text);
        image = findViewById(R.id.photo);

        Intent intent = getIntent();
        framilyId = intent.getLongExtra(FramilyProfile.ID_KEY, -1);
        memoryId = intent.getLongExtra(ID_MEMORY, -1);
        framily = dbSource.fetchFramilyByIndex(framilyId);
        memory = dbSource.fetchMemoryByIndex(memoryId);

        updateViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        dbSource.open();
        memory = dbSource.fetchMemoryByIndex(memoryId);
        updateViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        dbSource.close();
    }

    public void updateViews() {
        back.setText("< Back to " + framily.getNameFirst());
        title.setText(memory.getTitle());
        text.setText(memory.getText());
        if(memory.getImage() != null) {
            try {
                FileInputStream fis = openFileInput(memory.getFilename());
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                Bitmap rotBmap = ImageRotation.rotateImage(bmap,0);
                image.setImageBitmap(rotBmap);
                fis.close();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.backToPerson:
                finish();
                break;

            case R.id.edit:
                finish();
                intent = new Intent(this, AddEditMemoryActivity.class);
                intent.putExtra(ID_MEMORY, memoryId);
                intent.putExtra(FramilyProfile.ID_KEY, framilyId);
                startActivity(intent);
                break;

            case R.id.speaker:
                try {
                    player = new MediaPlayer();
                    //Log.d("rdudak", memory.getAudio() + "");
                    if(memory.getAudio()!=null){
                        player.setDataSource(memory.getAudio());
                        player.prepare();
                        player.start();
                    }
                    else{
                        Toast.makeText(this,"No Audio Recorded", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}