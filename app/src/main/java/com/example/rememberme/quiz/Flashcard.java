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

import androidx.appcompat.app.AppCompatActivity;

import com.example.rememberme.R;
import com.example.rememberme.ui.quiz.QuizFragment;

public class Flashcard extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "result popup";
    private QuizQuestions questions;

    private TextView mQuestion;
    private FrameLayout questionCard;
    private Button exit;
    private Button seeNext;

    private int questionNum;
    private String answer;
    private Boolean onQuestion;

//    private AnimatorSet mSetRightOut;
//    private AnimatorSet mSetLeftIn;
//    private boolean mIsBackVisible = false;
//    private View mCardFrontLayout;
//    private View mCardBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        setTitle("Review");

        Intent myintent = getIntent();

        questions = new QuizQuestions(myintent.getParcelableArrayListExtra(QuizFragment.QUIZ_KEY));
        questionNum = 0;

        exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(this);

        mQuestion = (TextView)findViewById(R.id.question);
        seeNext = (Button) findViewById(R.id.see_answer);
        seeNext.setOnClickListener(this);

//        mCardBackLayout = findViewById(R.id.card_back);
//        mCardFrontLayout = findViewById(R.id.card_front);
        //loadAnimations();
        //changeCameraDistance();

        onQuestion = true;
        updateQuestion();
    }



//    private void changeCameraDistance() {
//        int distance = 8000;
//        float scale = getResources().getDisplayMetrics().density * distance;
//        mCardFrontLayout.setCameraDistance(scale);
//        mCardBackLayout.setCameraDistance(scale);
//    }

//    private void loadAnimations() {
//        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.start_flip);
//        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.end_flip);
//    }

    private void updateQuestion(){

        if(onQuestion){
            mQuestion.setText(questions.getQuestion(questionNum).question);
            seeNext.setText(R.string.see);
            onQuestion = false;
        }else{
            mQuestion.setText(questions.getQuestion(questionNum).correct_answer);
            if (questionNum < questions.getSize() - 1) {
                seeNext.setText(R.string.next);
            }else{
                seeNext.setText("Finish");
            }
            questionNum ++;
            onQuestion = true;
        }

    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.see_answer:
                if (questionNum < questions.getSize()){
                    updateQuestion();
                }
                else{
                    finish();
                }
        }

    }


}
