package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rememberme.DB.RememberMeDbSource;

public class ViewMemory extends AppCompatActivity implements View.OnClickListener {

    public static final String ID_MEMORY = "id_memory";

    RememberMeDbSource dbSource;

    Button back;
    Button edit;
    TextView title;
    TextView text;
    ImageView image;

    long framilyId;
    long memoryId;

    Framily framily;
    Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        back = findViewById(R.id.backToPerson);
        back.setOnClickListener(this);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
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

    public void updateViews() {
        back.setText("< Back to " + framily.getNameFirst());
        title.setText(memory.getTitle());
        text.setText(memory.getText());
       // image.setImageURI();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.backToPerson:
                finish();
                break;

            case R.id.edit:
                intent = new Intent(this, AddEditMemoryActivity.class);
                intent.putExtra(ID_MEMORY, memoryId);
                startActivity(intent);
                break;
        }
    }
}