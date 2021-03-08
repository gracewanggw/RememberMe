package com.example.rememberme.ui.quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.app.AlertDialog;

import androidx.fragment.app.DialogFragment;

import com.example.rememberme.R;
import com.example.rememberme.quiz.Flashcard;
import com.example.rememberme.quiz.Question;
import com.example.rememberme.quiz.Quiz;

import java.util.ArrayList;

public class MyAlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    ArrayList<Question> quiz;
    boolean fib;
    int type;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View content;
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        quiz = bundle.getParcelableArrayList(QuizFragment.QUIZ_KEY);
        fib = bundle.getBoolean("fib");
        type = bundle.getInt(QuizFragment.QUIZ_TYPE_KEY);

        if(title.equals("No Review")){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            content = inflater.inflate(R.layout.dialog_layout_review, null);

            AlertDialog.Builder noneBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
            noneBuilder.setView(content);

            // add the buttons
            noneBuilder.setNegativeButton("Done", this);

            return noneBuilder.create();
        }else {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            content = inflater.inflate(R.layout.dialog_quiz, null);

            AlertDialog.Builder modeBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
            modeBuilder.setView(content);

            // add the buttons
            modeBuilder.setPositiveButton("Flashcard Mode", this);
            modeBuilder.setNeutralButton("Quiz Mode", this);

            return modeBuilder.create();
        }
    }

    public void onClick(DialogInterface dialogInterface, int item){

        if(item == DialogInterface.BUTTON_POSITIVE){

            Intent intent = new Intent(getContext(), Flashcard.class);
            intent.putExtra(QuizFragment.QUIZ_KEY, quiz);
            startActivity(intent);

            //flash.onFlashClick();
        }  else if (item == DialogInterface.BUTTON_NEUTRAL){
            Intent intent = new Intent(getContext(), Quiz.class);
            intent.putExtra(QuizFragment.FILL_IN_BLANK, fib);
            intent.putExtra(QuizFragment.QUIZ_KEY, quiz);
            intent.putExtra(QuizFragment.QUIZ_TYPE_KEY, type);
            startActivity(intent);
        }
        else if (item == DialogInterface.BUTTON_NEGATIVE){
        }

    }


}