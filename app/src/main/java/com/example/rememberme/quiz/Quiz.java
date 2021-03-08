package com.example.rememberme.quiz;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.R;
import com.example.rememberme.ui.quiz.QuizFragment;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//Quiz Activity
public class Quiz extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "result popup";
    private QuizQuestions questions;

    private TextView mQuestion;
    private FrameLayout questionCard;
    private Button exit;
    private Button c1;
    private Button c2;
    private Button c3;
    private Button c4;
    private EditText input;
    private Button submit;

    private int questionNum;
    private String answer;

    private int type;
    private int correct_answers;
    private int wrong_answers;
    private Float score;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    public boolean fillIn = false;
    public ArrayList<Question> quiz;
    public ArrayList<String> responses = new  ArrayList<String>();
    private boolean correct = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myintent = getIntent();
        //uses the key to know what message to get
        fillIn = myintent.getBooleanExtra(QuizFragment.FILL_IN_BLANK, false);
        quiz = myintent.getParcelableArrayListExtra(QuizFragment.QUIZ_KEY);
        type = myintent.getIntExtra(QuizFragment.QUIZ_TYPE_KEY, 0);

        questions = new QuizQuestions(quiz, this.getApplicationContext());
        correct_answers = 0;
        wrong_answers = 0;
        score = 0f;
        questionNum = 0;

        if (fillIn == false) {
            setContentView(R.layout.activity_quiz_mc);

            c1 = (Button) findViewById(R.id.op1);
            c2 = (Button) findViewById(R.id.op2);
            c3 = (Button) findViewById(R.id.op3);
            c4 = (Button) findViewById(R.id.op4);

            c1.setOnClickListener(this);
            c2.setOnClickListener(this);
            c3.setOnClickListener(this);
            c4.setOnClickListener(this);
        }else if (fillIn == true){

            setContentView(R.layout.activity_quiz_fillin);

            input = (EditText)findViewById(R.id.fillin_ans);
            submit = (Button) findViewById(R.id.submit_ans);
            submit.setOnClickListener(this);

        }else{
            Log.d("debug", "invalid quiz type");
        }


        exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(this);

        mQuestion = (TextView)findViewById(R.id.question);
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);

        //loadAnimations();
        //changeCameraDistance();

        updateQuestion();

    }



    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.start_flip);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.end_flip);
    }

    private void updateQuestion(){

        mQuestion.setText(questions.getQuestion(questionNum).question);
        answer = questions.getQuestion(questionNum).correct_answer;

        if(!fillIn) {
            c1.setText(questions.getQuestion(questionNum).op1);
            c2.setText(questions.getQuestion(questionNum).op2);
            c3.setText(questions.getQuestion(questionNum).op3);
            c4.setText(questions.getQuestion(questionNum).op4);
        }else{
            input.setHint(formatHint(questions.getQuestion(questionNum).quesType));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.op1:
                responses.add(c1.getText().toString());
                if(c1.getText().toString().equals(answer)) {
                    c1.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op2:
                responses.add(c2.getText().toString());
                if(c2.getText().toString().equals(answer)){
                    c2.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op3:
                responses.add(c3.getText().toString());
                if(c3.getText().toString().equals(answer)) {
                    c3.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op4:
                responses.add(c4.getText().toString());
                if(c4.getText().toString().equals(answer)){
                    c4.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.submit_ans:
                responses.add(input.getText().toString());

                String comp1 = input.getText().toString().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
                String comp2 = answer.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

                correct = comp1.equals(comp2);

                input.getText().clear();
                break;
//            case R.id.question_sec:
//                if (!mIsBackVisible) {
//                    mSetRightOut.setTarget(mCardFrontLayout);
//                    mSetLeftIn.setTarget(mCardBackLayout);
//                    mSetRightOut.start();
//                    mSetLeftIn.start();
//                    mIsBackVisible = true;
//                } else {
//                    mSetRightOut.setTarget(mCardBackLayout);
//                    mSetLeftIn.setTarget(mCardFrontLayout);
//                    mSetRightOut.start();
//                    mSetLeftIn.start();
//                    mIsBackVisible = false;
//                }
               // break;
            }


        if(view.getId() != R.id.exit ) {
            if (correct) {
                correct_answers++;
                correct = false;
            } else {
                wrong_answers++;
            }

            questionNum++;
            if (questionNum < questions.getSize()) {
                updateQuestion();
            } else {

                endQuiz();
            }
        }

    }

    public void endQuiz(){
        score = 100.0f * ((float)correct_answers/(float)questions.getSize());

        Intent intent = new Intent( this, QuizResult.class);
        Bundle bundle = new Bundle();

        bundle.putInt(QuizFragment.QUIZ_TYPE_KEY, type);
        bundle.putInt(QuizResult.CORRECT_KEY, correct_answers);
        bundle.putInt(QuizResult.WRONG_KEY, wrong_answers);
        bundle.putFloat(QuizResult.PERCENT_KEY, score);
        bundle.putStringArrayList("response", responses);
        bundle.putParcelableArrayList("quiz", quiz);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public String formatHint(String qType){
        String format = "";
        switch (qType) {
            case "relationship":
               format = "ex. friend";
               break;
            case "age":
                format = "ex. 22";
                break;
            case "birthday":
                format = "ex. September 9 1998";
                break;
            case "location":
                format = "ex. Portland, OR";
                break;
        }
        return format;
    }



}
