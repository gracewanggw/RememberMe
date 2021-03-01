package com.example.rememberme;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditUserProfileActivity extends AppCompatActivity{

    Button cancelBtn;
    Button saveBtn;
    Calendar myCalendar;

    EditText nameFirst;
    EditText nameLast;
    EditText age;
    EditText birthday;
    EditText location;
    EditText contact1;
    EditText contact2;

    ImageView profilePhoto;
    RoundImage roundedImage;

    String fName;
    String lName;
    String ageStr;
    String locationStr;
    String phone1;
    String phone2;

    public final static String FIRST_NAME_KEY = "1st name";
    public final static String LAST_NAME_KEY = "last name";
    public final static String PHONE1_KEY = "phone 1";
    public final static String PHONE2_KEY = "phone 2";
    public final static String LOCATION_KEY = "location key";
    public final static String AGE_KEY = "age key";
    public final static String BIRTHDAY_KEY = "bday key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        myCalendar = Calendar.getInstance();

        profilePhoto = (ImageView) findViewById(R.id.photo_profile);
        profilePhoto = (ImageView) findViewById(R.id.photo);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
        roundedImage = new RoundImage(bm);
        profilePhoto.setImageDrawable(roundedImage);

        nameFirst = findViewById(R.id.name_first);
        nameLast = findViewById(R.id.name_last);
        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
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
                finish();
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("gwang","birthday on click");
                new DatePickerDialog(EditUserProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    private void updateLabel() {
        String myFormat = "MMMM dd yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(sdf.format(myCalendar.getTime()));
    }

    public void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        fName = sharedPreferences.getString(FIRST_NAME_KEY,"");
        lName = sharedPreferences.getString(LAST_NAME_KEY,"");
        phone1 = sharedPreferences.getString(PHONE1_KEY,"");
        phone2 = sharedPreferences.getString(PHONE2_KEY,"");
        locationStr = sharedPreferences.getString(LOCATION_KEY,"");
        ageStr = sharedPreferences.getString(AGE_KEY,"");

    }

    public void saveEntry() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editors = sharedPreferences.edit();

        editors.clear();
        editors.putString(FIRST_NAME_KEY,nameFirst.getText().toString());
        editors.putString(LAST_NAME_KEY,nameLast.getText().toString());
        editors.putString(PHONE1_KEY,contact1.getText().toString());
        editors.putString(PHONE2_KEY,contact2.getText().toString());
        editors.putString(LOCATION_KEY,location.getText().toString());
        editors.putString(AGE_KEY,age.getText().toString());
        editors.putString(BIRTHDAY_KEY,birthday.getText().toString());

        editors.commit();

        Toast.makeText(this,"Data saved", Toast.LENGTH_SHORT).show();
        Log.d("gwang","saved data ");
    }
}