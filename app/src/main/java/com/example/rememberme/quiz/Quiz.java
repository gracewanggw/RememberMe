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

    private int type = 0;
    private int correct_answers;
    private int wrong_answers;
    private Float score;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    public boolean fillIn = false;
    public int quiz_type;

    private boolean correct = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent myintent = getIntent();
        //uses the key to know what message to get
        fillIn = myintent.getBooleanExtra(QuizFragment.FILL_IN_BLANK, false);
        quiz_type = myintent.getIntExtra(QuizFragment.QUIZ_TYPE_KEY, -1);
        if (quiz_type == 0) {
            type = 0;
        } else if (quiz_type == 1) {
            type = 1;
        }else{
            Log.d("DEBUG", "not valid quiz type");
        }

        questions = new QuizQuestions();
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

        if(fillIn = false) {
            c1.setText(questions.getQuestion(questionNum).op1);
            c2.setText(questions.getQuestion(questionNum).op2);
            c3.setText(questions.getQuestion(questionNum).op3);
            c4.setText(questions.getQuestion(questionNum).op4);
        }else{
            Log.d("debug", "invalid quiz type");
        }

    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.op1:
                if(c1.getText().toString().equals(answer)) {
                    c1.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op2:
                if(c2.getText().toString().equals(answer)){
                    c2.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op3:
                if(c3.getText().toString().equals(answer)) {
                    c3.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op4:
                if(c4.getText().toString().equals(answer)){
                    c4.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.submit_ans:
                Log.d("correct", input.getText().toString());
                if(input.getText().toString().toLowerCase().equals(answer.toLowerCase())){
                    correct = true;
                }
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

        if (correct){
            correct_answers++;
            correct = false;
        }else{
            wrong_answers++;
        }

        questionNum ++;
        if (questionNum < questions.getSize()){
            updateQuestion();
        }
        else{
           endQuiz();
        }

    }

    public void endQuiz(){
        score = 100.0f * ((float)correct_answers/(float)questions.getSize());

        Intent intent = new Intent( this, QuizResult.class);
        Bundle bundle = new Bundle();

        bundle.putInt(QuizResult.QUIZ_KEY, QuizResult.PERSON);
        bundle.putInt(QuizResult.CORRECT_KEY, correct_answers);
        bundle.putInt(QuizResult.WRONG_KEY, wrong_answers);
        bundle.putFloat(QuizResult.PERCENT_KEY, score);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


}
