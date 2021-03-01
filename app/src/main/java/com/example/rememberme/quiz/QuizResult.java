package com.example.rememberme.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.rememberme.R;
import com.example.rememberme.ui.quiz.QuizFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Dialog to show the result from a quiz after done
public class QuizResult extends AppCompatActivity implements View.OnClickListener {

    public static final int PERSON = 0;
    public static final int REVIEW = 1;
    public static final String QUIZ_KEY = "quiz type";

    public static final String CORRECT_KEY = "correct";
    public static final String WRONG_KEY = "wrong";
    public static final String PERCENT_KEY = "percent";


//    public static int MODE = Context.MODE_PRIVATE;
//    public SharedPreferences preferences;
//    public static final String PREFERENCE_NAME = "saved info";

    private int type;
    private int correctans;
    private int wrongans;
    private float percentans;

    private TextView correctCt;
    private TextView wrongCt;
    private TextView percent;
    private ListView responses;
    ArrayList<String> ans;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        setTitle("Quiz Results");

        Intent myintent = getIntent();
        Bundle bundle = myintent.getExtras();
        //uses the key to know what message to get
        type = bundle.getInt(QUIZ_KEY, -1);
        correctans = bundle.getInt(CORRECT_KEY, -1);
        wrongans = bundle.getInt(WRONG_KEY, -1);
        percentans = bundle.getFloat(PERCENT_KEY, -10f);
        ans = bundle.getStringArrayList("response");
        //preferences = getActivity().getSharedPreferences("MySharedPref", MODE);

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

        renderQuestions(ans);
    }

    public void renderQuestions(ArrayList<String> answers){
        ArrayList<ArrayList<String>> toPass = new ArrayList<ArrayList<String>> ();
        QuizQuestions questions = new QuizQuestions();
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
                Log.d("fjx", "clicked");
                updateReviewSet();
                finish();
                break;
        }
    }

    public void updateReviewSet(){
        //do lofix for updating the set here
    }

}






