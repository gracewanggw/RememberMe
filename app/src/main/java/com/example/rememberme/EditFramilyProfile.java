package com.example.rememberme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.DB.FramilyDbSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditFramilyProfile extends AppCompatActivity implements View.OnClickListener{
    ImageView photo;
    RoundImage roundedImage;
    Calendar myCalendar;


    Button cancelBtn;
    Button saveBtn;
    Button remove;
    TextView addMemory;

    FramilyDbSource dbSource;
    public static Framily framily;
    int id;

    EditText nameFirst;
    EditText nameLast;
    EditText relationship;
    EditText age;
    EditText birthday;
    EditText location;
    EditText phone;
    ArrayList<Integer> memories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_framily_profile);
        myCalendar = Calendar.getInstance();

        dbSource = new FramilyDbSource(this);
        dbSource.open();

        nameFirst = findViewById(R.id.name_first);
        nameLast = findViewById(R.id.name_last);
        relationship = findViewById(R.id.relationship);
        age = findViewById(R.id.age);
        birthday = findViewById(R.id.birthday);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);

        Intent intent = getIntent();
        id = intent.getIntExtra(FramilyProfile.ID_KEY, -1);
        if (id < 0) {
            framily = new Framily();
            memories = new ArrayList<Integer>();
        }

        else {
            Log.d("rdudak", id + "");
            framily = dbSource.fetchEntryByIndex(id);
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

        cancelBtn = (Button)findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(this);
        saveBtn = (Button)findViewById(R.id.save);
        saveBtn.setOnClickListener(this);
        remove = findViewById(R.id.remove);
        remove.setOnClickListener(this);
        addMemory = findViewById(R.id.add_memory);
        addMemory.setOnClickListener(this);

        photo = (ImageView) findViewById(R.id.photo);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
        roundedImage = new RoundImage(bm);
        photo.setImageDrawable(roundedImage);
    }

    public void loadData() {
        nameFirst.setText(framily.nameFirst);
        nameLast.setText(framily.nameLast);
        relationship.setText(framily.getRelationship());
        age.setText(framily.getAge() + "");
        birthday.setText(framily.getBirthday());
        location.setText(framily.getLocation());
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
                intent = new Intent(this, AddMemoryActivity.class);
                intent.putExtra(FramilyProfile.ID_KEY, framily.getId());
                startActivity(intent);
                break;

            case R.id.remove:
                askRemove(this);
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
            dbSource.updateEntry(id);
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            dbSource.insertFramily(framily);
            Toast.makeText(this, "New Framily Member Saved", Toast.LENGTH_SHORT).show();
            id = dbSource.fetchLastEntry().getId();
        }
    }

    public void askRemove(Context context) {
        final String field = "Are you sure you want to remove " + nameFirst.getText().toString() + "?";
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(field)
                .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbSource.removeEntry(id);
                        finish();
                    }
                })
                .setNegativeButton("No, go back!", null)
                .create();
        dialog.show();
    }
}