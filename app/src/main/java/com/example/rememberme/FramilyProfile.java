package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rememberme.DB.FramilyDbHelper;
import com.example.rememberme.DB.FramilyDbSource;

import java.util.ArrayList;

public class FramilyProfile extends AppCompatActivity implements View.OnClickListener {

    FramilyDbSource dbSource;
    private Framily framily;
    int framilyId;

    ImageView photo;
    RoundImage roundedImage;
    private MemoriesAdapter memoriesAdapter;
    GridView gridView;
    ArrayList<Integer> memories;

    TextView name;
    TextView relationship;
    TextView age;
    TextView birthday;
    TextView location;

    Button call;
    Button back;
    Button quiz;
    TextView edit;
    TextView addMemory;

    public static final String ID_KEY = "id_key";

    public static final String IMAGE_KEY = "image";
    public static final String POSITION_KEY = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framily_profile);

        dbSource = new FramilyDbSource(this);
        dbSource.open();
        Intent intent = getIntent();
        framilyId = intent.getIntExtra(ID_KEY, -1);
        Log.d("rdudak", "ID = " + framilyId);
        if(framilyId >= 0) {
            framily = dbSource.fetchEntryByIndex(framilyId);
            Log.d("rdudak", framily.toString());
        }
        else
            framily = new Framily();

        name = findViewById(R.id.name);
        name.setText(framily.getNameFirst() + " " + framily.getNameLast());
        relationship = findViewById(R.id.relationship);
        relationship.setText(framily.getRelationship());
        age = findViewById(R.id.age);
        age.setText(framily.getAge() + "");
        birthday = findViewById(R.id.birthday);
        birthday.setText(framily.getBirthday());
        location = findViewById(R.id.location);
        location.setText(framily.getLocation());

        call = findViewById(R.id.call);
        call.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        quiz = findViewById(R.id.quiz);
        quiz.setOnClickListener(this);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        addMemory = findViewById(R.id.add_memory);
        addMemory.setOnClickListener(this);

        memories = framily.getMemories();
        memoriesAdapter = new MemoriesAdapter(this, memories);
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(memoriesAdapter);

        name.setText(framily.getNameFirst() + " " + framily.getNameLast());
        relationship.setText(framily.getRelationship());
        age.setText(framily.getAge() + "");
        birthday.setText(framily.getBirthday());
        location.setText(framily.getLocation());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //opens the GridItemActivity when a picture is clicked
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), GridItem.class);
                intent.putExtra(IMAGE_KEY, memories.get(position));
                intent.putExtra(POSITION_KEY, position);
                startActivity(intent);
            }
        });

        photo = (ImageView) findViewById(R.id.photo);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
        roundedImage = new RoundImage(bm);
        photo.setImageDrawable(roundedImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.call:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + framily.getPhoneNumber()));
                startActivity(intent);
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.quiz:
                //TODO: Go to quiz
                break;

            case R.id.edit:
                intent = new Intent(this, EditFramilyProfile.class);
                intent.putExtra(ID_KEY, framily.getId());
                startActivity(intent);
                break;

            case R.id.add_memory:
                intent = new Intent(this, AddMemoryActivity.class);
                intent.putExtra(ID_KEY, framily.getId());
                startActivity(intent);
                break;
        }
    }
}