package com.example.rememberme.quiz;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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
    private int score;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myintent = getIntent();
        //type = myintent.getIntExtra(QuizFragment.TYPE_KEY, 0);

        questions = new QuizQuestions();
        correct_answers = 0;
        wrong_answers = 0;
        score = 0;
        questionNum = 0;



        if (type == 0) {
            setContentView(R.layout.activity_quiz_mc);

            c1 = (Button) findViewById(R.id.op1);
            c2 = (Button) findViewById(R.id.op2);
            c3 = (Button) findViewById(R.id.op3);
            c4 = (Button) findViewById(R.id.op4);

            c1.setOnClickListener(this);
            c2.setOnClickListener(this);
            c3.setOnClickListener(this);
            c4.setOnClickListener(this);
        }else if (type == 1){

            setContentView(R.layout.activity_quiz_fillin);

            submit = (Button) findViewById(R.id.submit_ans);
            submit.setOnClickListener(this);

        }else{
            Log.d("debug", "invalid quiz type");
        }

        mQuestion = (TextView)findViewById(R.id.question);
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);

        loadAnimations();
        changeCameraDistance();

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

        if(type == 0) {
            c1.setText(questions.getQuestion(questionNum).op1);
            c2.setText(questions.getQuestion(questionNum).op2);
            c3.setText(questions.getQuestion(questionNum).op3);
            c4.setText(questions.getQuestion(questionNum).op4);
        }else{
            Log.d("debug", "invalid quiz type");
        }

    }

    public void onClick(View view) {

        boolean correct = false;
        switch (view.getId()) {
            case R.id.op1:
                if(c1.getText() == answer) {
                    c1.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op2:
                if(c2.getText() == answer){
                    c2.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op3:
                if(c3.getText() == answer) {
                    c3.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.op4:
                if(c4.getText() == answer){
                    c4.setElevation(10);
                    correct = true;
                }
                break;
            case R.id.submit_ans:
                if(input.getText().toString() == answer){
                    correct = true;
                }
                break;
            case R.id.question_sec:
                if (!mIsBackVisible) {
                    mSetRightOut.setTarget(mCardFrontLayout);
                    mSetLeftIn.setTarget(mCardBackLayout);
                    mSetRightOut.start();
                    mSetLeftIn.start();
                    mIsBackVisible = true;
                } else {
                    mSetRightOut.setTarget(mCardBackLayout);
                    mSetLeftIn.setTarget(mCardFrontLayout);
                    mSetRightOut.start();
                    mSetLeftIn.start();
                    mIsBackVisible = false;
                }
                break;
            }

        if (correct){
            correct_answers++;
        }else{
            wrong_answers++;
        }

        questionNum ++;
        if (questionNum < questions.getQuestion(questionNum).total){
            updateQuestion();
        }
        else{
           endQuiz();
        }

    }

    public void endQuiz(){
        score = (int) correct_answers/questions.getQuestion(questionNum).total;

        QuizResult resultDialog = new QuizResult();
        Bundle bundle = new Bundle();

        bundle.putInt(QuizResult.QUIZ_KEY, resultDialog.PERSON);
        bundle.putInt(QuizResult.CORRECT_KEY, correct_answers);
        bundle.putInt(QuizResult.WRONG_KEY, wrong_answers);
        bundle.putInt(QuizResult.PERCENT_KEY, score);
        resultDialog.setArguments(bundle);
        resultDialog.show(this.getSupportFragmentManager(), TAG);
        Toast.makeText(this, "End of quiz", Toast.LENGTH_SHORT).show();
    }

}
