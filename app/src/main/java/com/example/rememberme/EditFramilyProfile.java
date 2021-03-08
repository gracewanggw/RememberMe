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
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.DB.RememberMeDbSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditFramilyProfile extends AppCompatActivity implements View.OnClickListener{
    ImageView photo;
    RoundImage roundedImage;
    String fileName = "framily_img.jpg";
    String tempImgFileName = "profile.jpg";
    File tempImgFile;

    File pictureFile;
    Uri imageUri;
    Bitmap bitmap;
    Calendar myCalendar;

    Button cancelBtn;
    Button saveBtn;
    Button remove;
    TextView addMemory;

    private MemoriesAdapter memoriesAdapter;
    GridView gridView;

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

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        fileName = "IMG_" + timeStamp + ".jpg";
        pictureFile = new File(getExternalFilesDir(null), fileName);
        imageUri = FileProvider.getUriForFile(this, "com.example.rememberme", pictureFile);
        tempImgFile = new File(getExternalFilesDir(null), tempImgFileName);


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

        try {
            FileInputStream fis = openFileInput(fileName);
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            roundedImage = new RoundImage(bmap);
            photo.setImageDrawable(roundedImage);
            fis.close();
        } catch (IOException e) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
            roundedImage = new RoundImage(bm);
            photo.setImageDrawable(roundedImage);
        }


        memoriesAdapter = new MemoriesAdapter(this, getMemories());
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(memoriesAdapter);

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //opens the GridItemActivity when a picture is clicked
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewMemory.class);
                intent.putExtra(ViewMemory.ID_MEMORY, memories.get(position));
                intent.putExtra(FramilyProfile.POSITION_KEY, position);
                intent.putExtra(FramilyProfile.ID_KEY, id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dbSource.open();
        memoriesAdapter = new MemoriesAdapter(this, getMemories());
        gridView.setAdapter(memoriesAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        dbSource.close();
    }

    public void loadData() {
        nameFirst.setText(framily.nameFirst);
        nameLast.setText(framily.nameLast);
        relationship.setText(framily.getRelationship());
        age.setText(framily.getAge() + "");
        birthday.setText(framily.getBirthday());
        location.setText(framily.getLocation());
        phone.setText(framily.getPhoneNumber());
        fileName = framily.getPhotoFileName();
        try {
        updateImageView(framily.getImage());
        } catch (Exception e) {
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

    public ArrayList<Memory> getMemories() {
        ArrayList<Memory> memoryItems = new ArrayList<Memory>();
        for (Long id: memories) {
            memoryItems.add(dbSource.fetchMemoryByIndex(id));
        }
        return memoryItems;
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
                    InputStream iStream = getContentResolver().openInputStream(imageUri);
                    byte[] image = getBytes(iStream);
                    framily.setImage(image);
                    updateImageView(image);
                    Log.d("rdudak", "camera bitmap saved: " + framily.getImage().toString());
                } catch (Exception e) {
                    Log.d("rdudak", "camera save failed");
                    e.printStackTrace();
                }
            }
            if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    bitmap.recycle();
                    //byte[] image = getBytes(iStream);
                    framily.setImage(image);
                    updateImageView(image);
                    Log.d("rdudak", "gallery bitmap saved: " + framily.getImage().toString());
                } catch (Exception e) {
                    Log.d("rdudak", "gallery save failed");
                    e.printStackTrace();
                }
            }
            photo.buildDrawingCache();
            Bitmap map = photo.getDrawingCache();
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
                photo.setImageDrawable(roundedImage);
                fis.close();
            } catch (IOException e) {
                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
                roundedImage = new RoundImage(bm);
                photo.setImageDrawable(roundedImage);
            }

        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void saveEntry() {
        dbSource.open();
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
        photo.buildDrawingCache();
        Bitmap map = photo.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);
            map.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            framily.setPhotoFileName(fileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (id >= 0) {
            dbSource.updateFramilyEntry(id, framily);
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            dbSource.insertFramily(framily);
            Toast.makeText(this, "New Framily Member Saved", Toast.LENGTH_SHORT).show();
            id = dbSource.fetchLastFramilyEntry().getId();
        }
        dbSource.close();
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

    public void updateImageView(byte[] image) {
        Log.d("gwang", "updateImageView");
        Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);

        Bitmap rotatedBmp = ImageRotation.rotateImage(bmp, 90);
//        roundedImage = new RoundImage(bmp);
//        photo.setImageDrawable(roundedImage);
        photo.setImageBitmap(rotatedBmp);

        if(bmp!=null){
            Log.d("gwang", "not null bmp");
            RoundImage nRoundImg = new RoundImage(bmp);
            photo.setImageDrawable(nRoundImg);
        }
        //delete when can make image round and show up
        photo.setImageBitmap(bmp);
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