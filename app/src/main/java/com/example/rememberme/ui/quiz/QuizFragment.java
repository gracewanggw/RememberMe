package com.example.rememberme.ui.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.Framily;
import com.example.rememberme.R;
import com.example.rememberme.RoundImage;
import com.example.rememberme.quiz.Question;
import com.example.rememberme.quiz.Quiz;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizFragment extends Fragment{

    private DashboardViewModel dashboardViewModel;
    RememberMeDbSource dataSource;


    LinearLayout quizAll;
    LinearLayout nameFace;
    LinearLayout birthdays;
    LinearLayout review;

    Button fill_in_blank;
    Button mult_choice;

    boolean fib = true;

    public static final String QUIZ_KEY = "givenquiz";
    public static final String QUIZ_TYPE_KEY = "quiztype";
    public static final int QUIZ_TYPE_ALL_KEY = 0;
    public static final int QUIZ_TYPE_BIRTHDAY_KEY = 1;
    public static final int QUIZ_TYPE_REVIEW_KEY = 2;
    public static final int QUIZ_TYPE_FACE_KEY = 3;

    public int quizType;
    public ArrayList<Question> quiz;

    public static final String FILL_IN_BLANK = "fill in blank";

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);


        dataSource = new RememberMeDbSource(getActivity().getApplicationContext());

        quizAll = root.findViewById(R.id.quiz_all);
        nameFace = root.findViewById(R.id.name_to_face);
        birthdays = root.findViewById(R.id.quiz_birthdays);
        review = root.findViewById(R.id.quiz_review);
        fill_in_blank = root.findViewById(R.id.fill_in_blank);
        mult_choice = root.findViewById(R.id.mc_button);
        //updateMasterLists();

        fill_in_blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fib = true;
                mult_choice.setBackgroundColor(Color.TRANSPARENT);
                fill_in_blank.setBackgroundResource(R.drawable.quiz_button1);
                fill_in_blank.setTextColor(Color.WHITE);
                mult_choice.setTextColor(Color.BLACK);
            }
        });

        mult_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fib = false;
                mult_choice.setBackgroundResource(R.drawable.quiz_button1);
                fill_in_blank.setBackgroundColor(Color.TRANSPARENT);
                fill_in_blank.setTextColor(Color.BLACK);
                mult_choice.setTextColor(Color.WHITE);
            }
        });

        quizAll.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        quizAll.setAlpha((float)0.4);
                        v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        quizAll.setAlpha((float)1);
                        quizType = QUIZ_TYPE_ALL_KEY;
                        quiz = createQuiz();
                        Intent intent = new Intent(getContext(), Quiz.class);
                        intent.putExtra(FILL_IN_BLANK, fib);
                        intent.putExtra(QUIZ_KEY, quiz);
                        intent.putExtra(QUIZ_TYPE_KEY, quizType);
                        startActivityForResult(intent, 0);
                        break;
                    default:
                        break;
                }
                return true;
            }

        });

        nameFace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    nameFace.setAlpha((float)0.4);
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    nameFace.setAlpha((float)1);

                    quizType = QUIZ_TYPE_FACE_KEY;
                    quiz = createQuiz();
                    if(quiz.size() == 0){
                        MyAlertDialogFragment myDialog = new MyAlertDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "Not Enough");
                        myDialog.setArguments(bundle);
                        myDialog.show(getFragmentManager(), "dialog");
                    }else if(fib){
                        MyAlertDialogFragment myDialog = new MyAlertDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "No Face Fill");
                        myDialog.setArguments(bundle);
                        myDialog.show(getFragmentManager(), "dialog");
                    } else{
                        Intent intent = new Intent(getContext(), Quiz.class);
                        intent.putExtra(FILL_IN_BLANK, fib);
                        intent.putExtra(QUIZ_KEY, quiz);
                        intent.putExtra(QUIZ_TYPE_KEY, quizType);
                        startActivityForResult(intent, 0);
                    }
                }
                return true;
            }
        });

        birthdays.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    birthdays.setAlpha((float)0.4);
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    birthdays.setAlpha((float)1);
                    quizType = QUIZ_TYPE_BIRTHDAY_KEY;
                    quiz = createQuiz();
                    if(quiz.size() == 0){
                        MyAlertDialogFragment myDialog = new MyAlertDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "Not Enough");
                        myDialog.setArguments(bundle);
                        myDialog.show(getFragmentManager(), "dialog");
                    }else {
                        Intent intent = new Intent(getContext(), Quiz.class);
                        intent.putExtra(FILL_IN_BLANK, fib);
                        intent.putExtra(QUIZ_KEY, quiz);
                        intent.putExtra(QUIZ_TYPE_KEY, quizType);
                        startActivityForResult(intent, 0);
                    }
                }
                return true;
            }
        });

        review.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                review.setAlpha((float)0.4);
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                review.setAlpha((float)1);
                quizType = QUIZ_TYPE_REVIEW_KEY;
                quiz = createQuiz();
                if(quiz.size() == 0){
                    MyAlertDialogFragment myDialog = new MyAlertDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "No Review");
                    myDialog.setArguments(bundle);
                    myDialog.show(getFragmentManager(), "dialog");
                }else {
                    MyAlertDialogFragment myDialog = new MyAlertDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fib", fib);
                    bundle.putParcelableArrayList(QUIZ_KEY, quiz);
                    bundle.putInt(QUIZ_TYPE_KEY, quizType);
                    bundle.putString("title", "Review Mode");
                    myDialog.setArguments(bundle);
                    myDialog.show(getFragmentManager(), "dialog");
                }
            }
            return true;
            }
        });

        return root;
    }

    public ArrayList<Question> createQuiz() {
        dataSource.open();
        ArrayList<Question> quizQuestionList = new ArrayList<Question>();

        String[] infoChoice = {"relationship", "age", "birthday", "location", "photo"};

        List<Framily> people = dataSource.fetchFramilyEntries();
        int quizLength = Math.min(people.size() * 2 , 10);
        ArrayList<String> visited;

        switch (quizType) {
            case QUIZ_TYPE_ALL_KEY:
                visited = new ArrayList<String>();
                while (quizQuestionList.size() < quizLength) {
                    int random = (int) (Math.random() * people.size());
                    Framily chosen = people.get(random);
                    String fact = infoChoice[new Random().nextInt(infoChoice.length)];
                    String key = chosen.getNameFirst()+chosen.getNameLast()+fact;
                    Question ques = createQuestion(chosen, fact);
                    if( !visited.contains(key) && ques != null) {
                        visited.add(key);
                        quizQuestionList.add(ques);
                    }
                }
                break;
            case QUIZ_TYPE_BIRTHDAY_KEY:
                visited = new ArrayList<String>();
                quizLength = 0;
                for(String birthday : dataSource.fetchFramilyColumn("birthday")){
                    if(birthday.length() != 0){
                        quizLength += 1;
                    }
                }

                //incase not enough for quiz
                if(quizLength < 2){
                    return quizQuestionList;
                }

                while (quizQuestionList.size() < quizLength) {
                    int random = (int) (Math.random() * people.size());
                    Framily chosen = people.get(random);
                    String fact = "birthday";
                    String key = chosen.getNameFirst()+chosen.getNameLast();
                    Question ques = createQuestion(chosen, fact);
                    if( !visited.contains(key) && ques != null) {
                        visited.add(key);
                        quizQuestionList.add(ques);
                    }
                }
                break;
            case QUIZ_TYPE_FACE_KEY:
                visited = new ArrayList<String>();
                quizLength = 0;
                for(String facepic : dataSource.fetchFramilyColumn("photo")){
                    try {
                        FileInputStream fis = getActivity().getApplicationContext().openFileInput(facepic);
                        Bitmap bmap = BitmapFactory.decodeStream(fis);
                        Drawable d = new BitmapDrawable(getActivity().getApplicationContext().getResources(), bmap);
                        Drawable.ConstantState c1 = d.getConstantState();
                        fis.close();

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable._pic);
                        RoundImage roundedImage = new RoundImage(bitmap);
                        Drawable defaultPic = new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap);
                        Drawable.ConstantState cdefault = defaultPic.getConstantState();

                        if (facepic.length() != 0 && !c1.equals(cdefault)) {
                            quizLength += 1;
                        }
                    }catch (Exception e){
                        if (facepic.length() != 0) {
                            quizLength += 1;
                        }
                    }
                }

                //incase not enough for quiz
                if(quizLength < 4){
                    return quizQuestionList;
                }

                while (quizQuestionList.size() < quizLength) {
                    int random = (int) (Math.random() * people.size());
                    Framily chosen = people.get(random);
                    String fact = "photo";
                    String key = chosen.getNameFirst()+chosen.getNameLast();
                    Question ques = createQuestion(chosen, fact);
                    if( !visited.contains(key) && ques != null) {
                        visited.add(key);
                        quizQuestionList.add(ques);
                    }
                }
                break;
            case QUIZ_TYPE_REVIEW_KEY:
                quizQuestionList = (ArrayList) dataSource.fetchQuestions();
                break;
                }


        dataSource.close();
        return quizQuestionList;
    }


    public Question createQuestion(Framily person, String fact){
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
        }


        if(mQuestion.getAnswer().length() == 0 || mQuestion.getAnswer().equals("0")){
            return null;
        }else {
            return mQuestion;
        }
    }


}