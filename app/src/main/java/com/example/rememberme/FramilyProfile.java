package com.example.remembermeryan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FramilyProfile extends AppCompatActivity implements View.OnClickListener {

    ImageView photo;
    RoundImage roundedImage;
    private MemoriesAdapter memoriesAdapter;
    private Framily framily;
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

    private static final String ID_KEY = "id_key";

    private static final String IMAGE_KEY = "image";
    private static final String POSITION_KEY = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framily_profile);

        name = findViewById(R.id.name);
        relationship = findViewById(R.id.relationship);
        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
        location = findViewById(R.id.location);

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

        framily = new Framily(); //TODO: Link to database
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
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.test_pic);
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

            case R.id.back:
                //TODO: Back to people

            case R.id.quiz:
                //TODO: Go to quiz

            case R.id.edit:
                intent = new Intent(this, EditFramilyProfile.class);
                intent.putExtra(ID_KEY, framily.getId());
                startActivity(intent);

            case R.id.add_memory:
                intent = new Intent(this, AddMemoryActivity.class);
                intent.putExtra(ID_KEY, framily.getId());
                startActivity(intent);
        }
    }
}