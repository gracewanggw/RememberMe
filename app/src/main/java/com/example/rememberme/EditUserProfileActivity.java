package com.example.rememberme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class EditUserProfileActivity extends AppCompatActivity{

    Button cancelBtn;
    Button saveBtn;

    EditText nameFirst;
    EditText nameLast;
    EditText age;
    EditText location;
    EditText contact1;
    EditText contact2;

    String fName;
    String lName;
    String ageStr;
    String locationStr;
    String phone1;
    String phone2;

    final static String SHARED_PREFS = "shared prefs";
    final static String FIRST_NAME_KEY = "1st name";
    final static String LAST_NAME_KEY = "last name";
    final static String PHONE1_KEY = "phone 1";
    final static String PHONE2_KEY = "phone 2";
    final static String LOCATION_KEY = "location key";
    final static String AGE_KEY = "age key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameFirst = findViewById(R.id.name_first);
        nameLast = findViewById(R.id.name_last);
        age = findViewById(R.id.age);
        location = findViewById(R.id.location);
        contact1= findViewById(R.id.phone1);
        contact2 = findViewById(R.id.phone2);

        cancelBtn = findViewById(R.id.cancel);
        saveBtn = findViewById(R.id.save);

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

        if(!fName.equals("")){
            nameFirst.setText(fName);
        }
        if(!lName.equals("")){
            nameLast.setText(lName);
        }
        if(!phone1.equals("")){
            contact1.setText(phone1);
        }
        if (!phone2.equals("")) {
            contact2.setText(phone2);
        }
        if (!locationStr.equals("")) {
            location.setText(locationStr);
        }
        if (!ageStr.equals("")) {
            age.setText(ageStr);
        }

    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        fName = sharedPreferences.getString(FIRST_NAME_KEY,"");
        lName = sharedPreferences.getString(LAST_NAME_KEY,"");
        phone1 = sharedPreferences.getString(PHONE1_KEY,"");
        phone2 = sharedPreferences.getString(PHONE2_KEY,"");
        locationStr = sharedPreferences.getString(LOCATION_KEY,"");
        ageStr = sharedPreferences.getString(AGE_KEY,"");

    }

    public void saveEntry() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editors = sharedPreferences.edit();

        editors.clear();
        editors.putString(FIRST_NAME_KEY,nameFirst.getText().toString());
        editors.putString(LAST_NAME_KEY,nameLast.getText().toString());
        editors.putString(PHONE1_KEY,contact1.getText().toString());
        editors.putString(PHONE2_KEY,contact2.getText().toString());
        editors.putString(LOCATION_KEY,location.getText().toString());
        editors.putString(AGE_KEY,age.getText().toString());

        editors.commit();

        Toast.makeText(this,"Data saved", Toast.LENGTH_SHORT).show();
        Log.d("gwang","saved data ");
    }
}