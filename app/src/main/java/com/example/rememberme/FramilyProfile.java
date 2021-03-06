package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rememberme.DB.RememberMeDbSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class FramilyProfile extends AppCompatActivity implements View.OnClickListener {

    RememberMeDbSource dbSource;
    private Framily framily;
    Long framilyId;

    ImageView photo;
    Uri imageUri;
    RoundImage roundedImage;
    private MemoriesAdapter memoriesAdapter;
    GridView gridView;
    ArrayList<Long> memories;

    TextView name;
    TextView relationship;
    TextView age;
    TextView birthday;
    TextView location;
    TextView phone;

    Button call;
    Button back;
    Button quiz;
    TextView edit;
    TextView addMemory;

    public static final String ID_KEY = "id_key";
    public static final String MEMORY_KEY = "memory";
    public static final String POSITION_KEY = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framily_profile);
        photo = (ImageView) findViewById(R.id.photo);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
           e.printStackTrace();
        }

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        Intent intent = getIntent();
        framilyId = intent.getLongExtra(ID_KEY, -1);
        Log.d("rdudak", "ID = " + framilyId);
        if(framilyId >= 0) {
            framily = dbSource.fetchFramilyByIndex(framilyId);
            Log.d("rdudak", framily.toString());
            if (framily.getImage() != null)
                updateImageView(framily.getImage());
            else {
                roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(),R.drawable._pic));
                photo.setImageDrawable(roundedImage);
            }
        }
        else {
            framily = new Framily();
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
            roundedImage = new RoundImage(bm);
            photo.setImageDrawable(roundedImage);
        }

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
        phone = findViewById(R.id.phone_number);
        phone.setText(framily.getPhoneNumber());

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

        memoriesAdapter = new MemoriesAdapter(this, getMemories());
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(memoriesAdapter);

       updateViews();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //opens the GridItemActivity when a picture is clicked
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewMemory.class);
                intent.putExtra(ViewMemory.ID_MEMORY, memories.get(position));
                intent.putExtra(POSITION_KEY, position);
                intent.putExtra(ID_KEY, framilyId);
                startActivity(intent);
            }
        });
    }

    public ArrayList<Memory> getMemories() {
        ArrayList<Memory> memoryItems = new ArrayList<Memory>();
        for (Long id: memories) {
            memoryItems.add(dbSource.fetchMemoryByIndex(id));
        }
        return memoryItems;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbSource.open();
        framily = dbSource.fetchFramilyByIndex(framilyId);
        memories = framily.getMemories();
        memoriesAdapter = new MemoriesAdapter(this, getMemories());
        gridView.setAdapter(memoriesAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        dbSource.close();
    }

    public void updateViews() {
        name.setText(framily.getNameFirst() + " " + framily.getNameLast());
        relationship.setText(framily.getRelationship());
        age.setText(framily.getAge() + "");
        birthday.setText(framily.getBirthday());
        location.setText(framily.getLocation());
        if (framily.getImage() != null)
            updateImageView(framily.getImage());
        else {
            roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(),R.drawable._pic));
            photo.setImageDrawable(roundedImage);
        }
    }

    public void updateImageView(byte[] image) {
        Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
        Bitmap rotatedBmp = ImageRotation.rotateImage(bmp, 90);
//        roundedImage = new RoundImage(bmp);
//        photo.setImageDrawable(roundedImage);
        photo.setImageBitmap(rotatedBmp);
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
                intent = new Intent(this, AddEditMemoryActivity.class);
                intent.putExtra(ID_KEY, framily.getId());
                startActivity(intent);
                break;
        }
    }
}