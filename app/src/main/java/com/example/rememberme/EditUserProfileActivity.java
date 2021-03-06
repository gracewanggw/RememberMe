package com.example.rememberme;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rememberme.DB.FramilyDbSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    public static final String IMG_URI_KEY = "urikey";

    public static final int CAMERA_REQUEST_CODE =  1;
    public static final int GALLERY_REQUEST_CODE = 0;
    private Uri tempImgUri;
    private Uri saveImgUri;
    private ImageView imageView;
    private String tempImgFileName = "profile.jpg";
    public static String saveImgFileName = "savedImage.jpg";

    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        checkPermissions();

        myCalendar = Calendar.getInstance();

        profilePhoto = (ImageView) findViewById(R.id.profile_photo);

        nameFirst = findViewById(R.id.name_first);
        nameLast = findViewById(R.id.name_last);
        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
        location = findViewById(R.id.location);
        contact1= findViewById(R.id.phone1);
        contact2 = findViewById(R.id.phone2);

        cancelBtn = findViewById(R.id.cancel);
        saveBtn = findViewById(R.id.save);

        File tempImgFile = new File(getExternalFilesDir(null), tempImgFileName);
        File saveImgFile = new File(getExternalFilesDir(null),saveImgFileName);
        tempImgUri = FileProvider.getUriForFile(this, "com.example.rememberme", tempImgFile);
        saveImgUri = FileProvider.getUriForFile(this,"com.example.rememberme",saveImgFile);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImageChange(v);
            }
        });

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

        if(savedInstanceState!=null){
            saveImgUri = Uri.parse(savedInstanceState.getString(IMG_URI_KEY));
            imageView.setImageURI(saveImgUri);
            //Log.d("gwang", saveImgUri.toString());
        }

        try {
            FileInputStream fis = openFileInput(saveImgFileName);
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            roundedImage = new RoundImage(bmap);
            profilePhoto.setImageDrawable(roundedImage);
            fis.close();
        } catch (IOException e) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
            roundedImage = new RoundImage(bm);
            profilePhoto.setImageDrawable(roundedImage);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(IMG_URI_KEY,saveImgUri.toString());
        //Log.d("gwang", saveImgUri.toString());
    }

    private void checkPermissions()
    {
        if(Build.VERSION.SDK_INT < 23)
            return;
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }
//
//    public void openDialog() {
//        final EditText input = new EditText(this);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Select profile image")
//                .setView(input)
//                .setPositiveButton("Take from camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        File tempImgFile = new File(getExternalFilesDir(null), tempImgFileName);
//                        tempImgUri = FileProvider.getUriForFile(context, "com.example.rememberme", tempImgFile);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri);
//                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
//                    }
//                })
//                .setNegativeButton("Select from gallery", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(pickPhoto , GALLERY_REQUEST_CODE);
//                    }
//                })
//                .create();
//        dialog.show();
//    }

    public void profileImageChange(View view)
    {
        String[] options = {"Open Camera", "Select from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Open Camera")) {
                    Log.d("rdudak", "camera selected");
                    File tempImgFile = new File(getExternalFilesDir(null), tempImgFileName);
                    tempImgUri = FileProvider.getUriForFile(context, "com.example.rememberme", tempImgFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
                else {
                    Log.d("rdudak", "gallery selected");
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , GALLERY_REQUEST_CODE);
                }
            }
        });
        builder.show();
        Log.d("rdudak", "The Change button has been clicked.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                saveImgUri = tempImgUri;
                profilePhoto.setImageURI(saveImgUri);
            }
            if (requestCode == GALLERY_REQUEST_CODE) {
                saveImgUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), saveImgUri);
                    RoundImage roundProfile = new RoundImage(bitmap);
                    profilePhoto.setImageDrawable(roundProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profilePhoto.setImageURI(null);
                profilePhoto.setImageURI(saveImgUri);
            }
            profilePhoto.buildDrawingCache();
            Bitmap map = profilePhoto.getDrawingCache();
            try {
                FileOutputStream fos = openFileOutput(tempImgFileName,MODE_PRIVATE);
                map.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            try {
                FileInputStream fis = openFileInput(tempImgFileName);
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                roundedImage = new RoundImage(bmap);
                profilePhoto.setImageDrawable(roundedImage);
                fis.close();
            } catch (IOException e) {
                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
                roundedImage = new RoundImage(bm);
                profilePhoto.setImageDrawable(roundedImage);
            }
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
        profilePhoto.buildDrawingCache();
        Bitmap map = profilePhoto.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(saveImgFileName,MODE_PRIVATE);
            map.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

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