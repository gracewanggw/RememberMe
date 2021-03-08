package com.example.rememberme.quiz;

import android.content.Intent;
import android.os.Bundle;

import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.R;
import com.example.rememberme.ui.quiz.QuizFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Dialog to show the result from a quiz after done
public class QuizResult extends AppCompatActivity implements View.OnClickListener {

    public static final String CORRECT_KEY = "correct";
    public static final String WRONG_KEY = "wrong";
    public static final String PERCENT_KEY = "percent";

    private QuizQuestions questions;
    private ArrayList<Question> quiz;
    private ArrayList<Question> toReview = new ArrayList<Question>();

    public static int type;
    private int correctans;
    private int wrongans;
    private float percentans;

    private TextView correctCt;
    private TextView wrongCt;
    private TextView percent;
    private ListView responses;
    ArrayList<String> ans;

    private RememberMeDbSource dbSource;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        setTitle("Quiz Results");

        dbSource = new RememberMeDbSource(getApplicationContext());

        Intent myintent = getIntent();
        Bundle bundle = myintent.getExtras();
        //uses the key to know what message to get
        type = bundle.getInt(QuizFragment.QUIZ_TYPE_KEY, -1);

        correctans = bundle.getInt(CORRECT_KEY, -1);
        wrongans = bundle.getInt(WRONG_KEY, -1);
        percentans = bundle.getFloat(PERCENT_KEY, -10f);
        ans = bundle.getStringArrayList("response");
        quiz = bundle.getParcelableArrayList("quiz");
        questions = new QuizQuestions(quiz, this.getApplicationContext());

        correctCt = (TextView)findViewById(R.id.numCorrect);
        wrongCt = (TextView)findViewById(R.id.numWrong);
        percent = (TextView)findViewById(R.id.percentage);
        responses = (ListView) findViewById(R.id.wrongAnswers);
        Button save_btn = (Button)findViewById(R.id.btnSave);

        correctCt.setText(correctans+"");
        wrongCt.setText(wrongans+"");
        String noDec = ""+ (int)percentans;
        percent.setText(noDec);
        save_btn.setOnClickListener(this);
        if(type == QuizFragment.QUIZ_TYPE_REVIEW_KEY){
            save_btn.setText(R.string.done);
        }
        renderQuestions(ans);
    }

    public void renderQuestions(ArrayList<String> answers){
        ArrayList<ArrayList<String>> toPass = new ArrayList<ArrayList<String>> ();
        for (int j = 0; j < answers.size(); j++){
            ArrayList<String> thisQues = new ArrayList<String>();
            thisQues.add(questions.getQuestion(j).question);
            thisQues.add(questions.getQuestion(j).correct_answer);
            thisQues.add(answers.get(j));
            toPass.add(thisQues);
        }

        QuestionAdapter mAdapter = new QuestionAdapter(this,
                toPass);

        // Assign the adapter to ListView
        responses.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                QuestionAdapter v = (QuestionAdapter)responses.getAdapter();
                ArrayList<Boolean> checked = v.mChecked;
                for (int i = 0; i < responses.getCount(); i++) {
                    if(checked.get(i)) {
                        toReview.add(quiz.get(i));
                    }
                }
                updateReviewSet();

                finish();
                break;
        }
    }

    public void updateReviewSet(){
        new Thread() {
            @Override
            public void run() {
                dbSource.open();
                //ArrayList<Question> allQuestions = (ArrayList)dbSource.fetchQuestions();
                for(int i = 0; i < toReview.size(); i++){
                    int quesid = (int)dbSource.getQuestionID(toReview.get(i).getmQuestion(), toReview.get(i).getPerson());

                    if (type == QuizFragment.QUIZ_TYPE_REVIEW_KEY){
                        //if in review already remove from review
                        if(quesid >= 0){
                            toReview.get(i).setReview(false);
                            dbSource.removeQuestion(quesid);
                        }else {
                           Log.d("debug", "Error question should be able to be removed");
                        }
                    }else{
                        //if in review already remove from review
                        toReview.get(i).setReview(true);
                        if (quesid < 0) {
                            dbSource.insertQuestion(toReview.get(i));
                        }
                    }
                }
                dbSource.close();
            }
        }.start();
        Log.d("fjx", ""+ toReview);
    }


}






