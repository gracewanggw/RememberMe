package com.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rememberme.DB.FramilyDbSource;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity{

    Button cancelBtn;
    Button saveBtn;

    FramilyDbSource dbSource;
    public static Framily framily;
    int id;

    EditText nameFirst;
    EditText nameLast;
    EditText relationship;
    EditText age;
    EditText birthday;
    EditText location;
    EditText contact1;
    EditText contact2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameFirst = findViewById(R.id.name_first);
        nameLast = findViewById(R.id.name_last);
        relationship = findViewById(R.id.relationship);
        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
        location = findViewById(R.id.location);
        contact1= findViewById(R.id.phone1);
        contact2 = findViewById(R.id.phone2);

        dbSource = new FramilyDbSource(this);
        dbSource.open();

        cancelBtn = findViewById(R.id.cancel);
        saveBtn = findViewById(R.id.save);

        framily = new Framily();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });

        loadData();


    }

    public void loadData() {
        //load existing data if available;

//        nameFirst.setText(framily.nameFirst);
//        nameLast.setText(framily.nameLast);
//        age.setText(framily.getAge() + "");
//        birthday.setText(framily.getBirthday());
//        location.setText(framily.getLocation());
    }

    public void saveEntry() {
        //save data
        
//        framily.setNameFirst(nameFirst.getText().toString());
//        framily.setNameLast(nameLast.getText().toString());
//        if (!age.getText().toString().equals(""))
//            framily.setAge(Integer.parseInt(age.getText().toString()));
//        else
//            framily.setAge(0);
//        framily.setBirthday(birthday.getText().toString());
//        framily.setPhoneNumber(phone.getText().toString());
//        framily.setLocation(location.getText().toString());
//        if (id >= 0) {
//            dbSource.updateEntry(id);
//            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            dbSource.insertFramily(framily);
//            Toast.makeText(this, "New Framily Member Saved", Toast.LENGTH_SHORT).show();
//            id = dbSource.fetchLastEntry().getId();
//        }
    }
}