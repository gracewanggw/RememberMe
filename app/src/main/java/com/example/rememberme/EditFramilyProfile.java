package com.example.rememberme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.DB.RememberMeDbSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditFramilyProfile extends AppCompatActivity implements View.OnClickListener{
    ImageView photo;
    RoundImage roundedImage;
    String fileName;
    File pictureFile;
    Uri imageUri;
    Bitmap bitmap;
    Calendar myCalendar;

    Button cancelBtn;
    Button saveBtn;
    Button remove;
    TextView addMemory;

    RememberMeDbSource dbSource;
    public static Framily framily;
    Long id;

    EditText nameFirst;
    EditText nameLast;
    EditText relationship;
    EditText age;
    EditText birthday;
    EditText location;
    EditText phone;
    ArrayList<Long> memories;

    public static final int CAMERA_REQUEST_CODE =  1;
    public static final int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_framily_profile);
        checkPermissions();
        myCalendar = Calendar.getInstance();

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        fileName = "IMG_" + timeStamp + ".jpg";
        pictureFile = new File(getExternalFilesDir(null), fileName);
        imageUri = FileProvider.getUriForFile(this, "com.example.rememberme", pictureFile);

        nameFirst = findViewById(R.id.name_first);
        nameLast = findViewById(R.id.name_last);
        relationship = findViewById(R.id.relationship);
        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        photo = (ImageView) findViewById(R.id.photo);

        cancelBtn = (Button)findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(this);
        saveBtn = (Button)findViewById(R.id.save);
        saveBtn.setOnClickListener(this);
        remove = findViewById(R.id.remove);
        remove.setOnClickListener(this);
        addMemory = findViewById(R.id.add_memory);
        addMemory.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getLongExtra(FramilyProfile.ID_KEY, -1);
        if (id < 0) {
            framily = new Framily();
            memories = new ArrayList<Long>();
            remove.setVisibility(View.GONE);
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
            roundedImage = new RoundImage(bitmap);
            photo.setImageDrawable(roundedImage);
        }

        else {
            Log.d("rdudak", id + "");
            framily = dbSource.fetchFramilyByIndex(id);
            memories = framily.getMemories();
            loadData();
        }

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
                new DatePickerDialog(EditFramilyProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void loadData() {
        nameFirst.setText(framily.nameFirst);
        nameLast.setText(framily.nameLast);
        relationship.setText(framily.getRelationship());
        age.setText(framily.getAge() + "");
        birthday.setText(framily.getBirthday());
        location.setText(framily.getLocation());
        phone.setText(framily.getPhoneNumber());
        try
        {
            if (imageUri != null) {
                FileInputStream fis = new FileInputStream(new File(fileName));
                bitmap = BitmapFactory.decodeStream(fis);
                roundedImage = new RoundImage(bitmap);
                photo.setImageDrawable(roundedImage);
                fis.close();
            }
        } catch (IOException e) {
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
            roundedImage = new RoundImage(bitmap);
            photo.setImageDrawable(roundedImage);
        }
    }

    private void updateLabel() {
        String myFormat = "MMMM dd yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;

            case R.id.save:
                saveEntry();
                intent = new Intent(this, FramilyProfile.class);
                intent.putExtra(FramilyProfile.ID_KEY, id);
                startActivity(intent);
                break;

            case R.id.add_memory:
                intent = new Intent(this, AddEditMemoryActivity.class);
                intent.putExtra(FramilyProfile.ID_KEY, framily.getId());
                startActivity(intent);
                break;

            case R.id.remove:
                askRemove(this);
        }
    }

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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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
                try {
                    photo.setImageURI(imageUri);
                    bitmap = BitmapFactory.decodeFileDescriptor(getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor());
                    roundedImage = new RoundImage(bitmap);
                    photo.setImageDrawable(roundedImage);
                    String imagePath = pictureFile.getAbsolutePath();
                    framily.setImage(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    imageUri = data.getData();
                    String imagePath = imageUri.toString();
                    bitmap = BitmapFactory.decodeFileDescriptor(getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor());
                    roundedImage = new RoundImage(bitmap);
                    photo.setImageDrawable(roundedImage);
//                    FileInputStream fis = openFileInput(imagePath);
//                    bitmap = BitmapFactory.decodeStream(fis);
//                    roundedImage = new RoundImage(bitmap);
//                    photo.setImageDrawable(roundedImage);
//                    fis.close();
                    framily.setImage(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveEntry() {
        framily.setNameFirst(nameFirst.getText().toString());
        framily.setNameLast(nameLast.getText().toString());
        framily.setRelationship(relationship.getText().toString());
        if (!age.getText().toString().equals(""))
            framily.setAge(Integer.parseInt(age.getText().toString()));
        else
            framily.setAge(0);
        framily.setBirthday(birthday.getText().toString());
        framily.setPhoneNumber(phone.getText().toString());
        framily.setLocation(location.getText().toString());
        framily.setMemories(memories);
        if (id >= 0) {
            dbSource.updateFramilyEntry(id, framily);
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            dbSource.insertFramily(framily);
            Toast.makeText(this, "New Framily Member Saved", Toast.LENGTH_SHORT).show();
            id = dbSource.fetchLastFramilyEntry().getId();
        }
    }

    public void askRemove(Context context) {
        final String field = "Are you sure you want to remove " + nameFirst.getText().toString() + "?";
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(field)
                .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbSource.removeFramily(id);
                        Intent intent = new Intent(EditFramilyProfile.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No, go back!", null)
                .create();
        dialog.show();
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
}