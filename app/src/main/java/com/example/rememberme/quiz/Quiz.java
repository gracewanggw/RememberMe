package com.example.rememberme.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.R;
import androidx.appcompat.app.AppCompatActivity;

//Quiz Activity
public class Quiz extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "result popup";
    private QuizQuestions questions;

    private TextView mQuestion;
    private Button c1;
    private Button c2;
    private Button c3;
    private Button c4;

    private int questionNum;
    private String answer;

    private int correct_answers;
    private int wrong_answers;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mc);

        questions = new QuizQuestions();
        correct_answers = 0;
        wrong_answers = 0;
        score = 0;
        questionNum = 0;

        mQuestion = (TextView) findViewById(R.id.question);
        c1 = (Button) findViewById(R.id.op1);
        c2 = (Button) findViewById(R.id.op2);
        c3 = (Button) findViewById(R.id.op3);
        c4 = (Button) findViewById(R.id.op4);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);

        updateQuestion();

    }

    private void updateQuestion(){
        mQuestion.setText(questions.getQuestion(questionNum).question);
        c1.setText(questions.getQuestion(questionNum).op1);
        c2.setText(questions.getQuestion(questionNum).op2);
        c3.setText(questions.getQuestion(questionNum).op3);
        c4.setText(questions.getQuestion(questionNum).op4);

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
            QuizResult resultDialog = new QuizResult();
            Bundle bundle = new Bundle();

            bundle.putInt(QuizResult.QUIZ_KEY, resultDialog.PERSON);
            resultDialog.setArguments(bundle);
            resultDialog.show(this.getSupportFragmentManager(), TAG);
            Toast.makeText(this, "End of quiz", Toast.LENGTH_SHORT).show();

        }


    }

}
