package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.rememberme.quiz.Question;
import com.example.rememberme.quiz.Quiz;
import com.example.rememberme.ui.quiz.MyAlertDialogFragment;
import com.example.rememberme.ui.quiz.QuizFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
    int infoCount;

    public static boolean removeLast = false;

    public static final int QUIZ_TYPE_PERSON_KEY = 4;
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
        Log.d("rdudak", "on create memories: " + memories);
        if (!removeLast && !memories.isEmpty()) {
            Log.d("rdudak", "empty: " + memories.isEmpty() + " Remove Last: " + removeLast);
            memoriesAdapter = new MemoriesAdapter(this, getMemories());
            gridView = (GridView)findViewById(R.id.gridview);
            gridView.setAdapter(memoriesAdapter);
        }
        else {
            Log.d("rdudak", "list empty, remove last = " + removeLast);
            memories = new ArrayList<Long>();
            framily.setMemories(memories);
            dbSource.updateFramilyEntry(framilyId, framily);
            memoriesAdapter = new MemoriesAdapter(this, new ArrayList<Memory>());
            gridView = (GridView)findViewById(R.id.gridview);
            gridView.setAdapter(memoriesAdapter);
            removeLast = false;
        }

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
        Log.d("rdudak", "framily memories: " + framily.getMemories());
        Log.d("rdudak", "getMemories() " + memories.toString());
        if(!memories.isEmpty()) {
            for (Long id: memories) {
                memoryItems.add(dbSource.fetchMemoryByIndex(id));
            }
        }
        return memoryItems;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbSource.open();
        framily = dbSource.fetchFramilyByIndex(framilyId);
        memories = framily.getMemories();
        if (!memories.isEmpty() && !removeLast) {
            Log.d("rdudak", "empty: " + memories.isEmpty() + " Remove Last: " + removeLast);
            memoriesAdapter = new MemoriesAdapter(this, getMemories());
            gridView = (GridView)findViewById(R.id.gridview);
            gridView.setAdapter(memoriesAdapter);
        }
        else {
            Log.d("rdudak", "list empty, remove last = " + removeLast);
            memories = new ArrayList<Long>();
            framily.setMemories(memories);
            dbSource.updateFramilyEntry(framilyId, framily);
            memoriesAdapter = new MemoriesAdapter(this, new ArrayList<Memory>());
            gridView = (GridView)findViewById(R.id.gridview);
            gridView.setAdapter(memoriesAdapter);
            removeLast = false;
        }
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
//        if (framily.getImage() != null)
//            updateImageView(framily.getImage());
//        else {
//            roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(),R.drawable._pic));
//            photo.setImageDrawable(roundedImage);
//        }
    }

    public void updateImageView(byte[] image) {
//        Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
//        Bitmap rotatedBmp = ImageRotation.rotateImage(bmp, 90);
//        roundedImage = new RoundImage(rotatedBmp);
//        photo.setImageDrawable(roundedImage);
      //  photo.setImageBitmap(rotatedBmp);

        try {
            FileInputStream fis = openFileInput(framily.getPhotoFileName());
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            roundedImage = new RoundImage(bmap);
            photo.setImageDrawable(roundedImage);
            fis.close();
        } catch (IOException e) {

        }
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
                int quizType = QUIZ_TYPE_PERSON_KEY;
                ArrayList<Question> personQuiz = makePeopleQuiz();

                if(personQuiz.size() < 2){
                    MyAlertDialogFragment myDialog = new MyAlertDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "No Face Fill");
                    myDialog.setArguments(bundle);
                    myDialog.show(getSupportFragmentManager(), "dialog");
                }else {
                    Intent intentquiz = new Intent(getApplicationContext(), Quiz.class);
                    intentquiz.putExtra(QuizFragment.FILL_IN_BLANK, false);
                    intentquiz.putExtra(QuizFragment.QUIZ_KEY, personQuiz);
                    intentquiz.putExtra(QuizFragment.QUIZ_TYPE_KEY, quizType);
                    startActivity(intentquiz);
                }
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


    public ArrayList<Question> makePeopleQuiz() {
        dbSource.open();
        ArrayList<Question> quizQuestionList = new ArrayList<Question>();

        String[] infoChoice = {"relationship", "age", "birthday", "location", "photo", "lname", "phone"};

        ArrayList<String> visited = new ArrayList<String>();

        infoCount = 0;
        while (infoCount < infoChoice.length) {
            String fact = infoChoice[new Random().nextInt(infoChoice.length)];
            if( !visited.contains(fact)){
                visited.add(fact);
                Question ques = makeQuestion(framily, fact);
                if (ques != null) {
                    quizQuestionList.add(ques);
                }
            }
        }
        dbSource.close();
        return quizQuestionList;
    }

    public Question makeQuestion(Framily person, String fact){
        Question mQuestion = new Question();
        mQuestion.setPerson(person.getNameFirst()+person.getNameLast());
        mQuestion.setQType(fact);
        mQuestion.setQDataType("String");
        mQuestion.setReview(false);

        switch (fact) {
            case "relationship":
                mQuestion.setmQuestion("What is your relationship with "+ person.getNameFirst() + " " + person.getNameLast()+"?");
                mQuestion.setADataType("String");
                mQuestion.setAnswer(person.getRelationship());
                break;
            case "age":
                mQuestion.setmQuestion("How old is "+ person.getNameFirst() + " " + person.getNameLast()+"?");
                mQuestion.setADataType("int");
                mQuestion.setAnswer(""+person.getAge());
                break;
            case "birthday":
                mQuestion.setmQuestion("When is "+ person.getNameFirst() + " " + person.getNameLast()+"'s birthday?");
                mQuestion.setADataType("String");
                mQuestion.setAnswer(person.getBirthday());
                break;
            case "location":
                mQuestion.setmQuestion("Where is "+ person.getNameFirst() + " " + person.getNameLast()+" living right now?");
                mQuestion.setADataType("String");
                mQuestion.setAnswer(person.getLocation());
                break;
            case "photo":
                mQuestion.setmQuestion("Who is " + person.getNameFirst() + " " + person.getNameLast() + "?");
                mQuestion.setADataType("Image");
                mQuestion.setAnswer(person.getPhotoFileName());
                break;
            case "phone":
                mQuestion.setmQuestion("What is " + person.getNameFirst() + " " + person.getNameLast() + "'s phone number?");
                mQuestion.setADataType("String");
                mQuestion.setAnswer(person.getPhoneNumber());
                break;
            case "lname":
                mQuestion.setmQuestion("What is " + person.getNameFirst() + "'s last name?");
                mQuestion.setADataType("String");
                mQuestion.setAnswer(person.getNameLast());
                break;
        }

        //Log.d("answerLenth", "lenth is: " + mQuestion.getAnswer().length());
        if(mQuestion.getAnswer().length() < 1 || mQuestion.getAnswer() == null || mQuestion.getAnswer().equals("0")){
            infoCount += 1;
            return null;
        }else {
            infoCount += 1;
            return mQuestion;
        }
    }
}