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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Dialog to show the result from a quiz after done
public class QuizResult extends AppCompatActivity implements View.OnClickListener {

    public static final int PERSON = 0;
    public static final int REVIEW = 1;
    public static final String QUIZ_KEY = "quiz type";

    public static final String CORRECT_KEY = "type of quiz";
    public static final String WRONG_KEY = "type of quiz";
    public static final String PERCENT_KEY = "type of quiz";


//    public static int MODE = Context.MODE_PRIVATE;
//    public SharedPreferences preferences;
//    public static final String PREFERENCE_NAME = "saved info";

    private int type;
    private int correctans;
    private int wrongans;
    private int percentans;

    private TextView correctCt;
    private TextView wrongCt;
    private TextView percent;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        setTitle("Quiz Results");

        Intent myintent = getIntent();
        Bundle bundle = myintent.getExtras();
        //uses the key to know what message to get
        type = bundle.getInt(QUIZ_KEY, 0);
        correctans = bundle.getInt(CORRECT_KEY, 0);
        wrongans = bundle.getInt(WRONG_KEY, 0);
        percentans = bundle.getInt(PERCENT_KEY, 0);
        //preferences = getActivity().getSharedPreferences("MySharedPref", MODE);

        Log.d("fjx", "c"+correctans);
        Log.d("fjx", "w"+wrongans);
        Log.d("fjx", "p"+percentans);


        correctCt = (TextView)findViewById(R.id.numCorrect);
        wrongCt = (TextView)findViewById(R.id.numWrong);
        percent = (TextView)findViewById(R.id.percentage);
        Button button = (Button)findViewById(R.id.btnSave);

//        correctCt.setText(correctans);
//        wrongCt.setText(wrongans);
//        percent.setText(percentans);
        button.setOnClickListener(this);
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






