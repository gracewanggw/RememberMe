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
import com.example.rememberme.quiz.Quiz;

public class MyAlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    int type;
    boolean fib;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View content;
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        type = bundle.getInt("quizType");
        fib = bundle.getBoolean("fib");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        content = inflater.inflate(R.layout.dialog_quiz, null);

        AlertDialog.Builder modeBuilder = new AlertDialog.Builder(getActivity());
        modeBuilder.setView(content);

        // add the buttons
        modeBuilder.setPositiveButton("Flashcard Mode", this);
        modeBuilder.setNeutralButton("Quiz Mode", this);

        return modeBuilder.create();
    }

    public void onClick(DialogInterface dialogInterface, int item){

        if(item == DialogInterface.BUTTON_POSITIVE){
//            Intent intent = new Intent(getContext(), Flashcard.class);
//            startActivity(intent);

            //flash.onFlashClick();
        }  else if (item == DialogInterface.BUTTON_NEUTRAL){
            Intent intent = new Intent(getContext(), Quiz.class);
            intent.putExtra(QuizFragment.FILL_IN_BLANK, fib);
            intent.putExtra(QuizFragment.QUIZ_TYPE_KEY, type);
            startActivity(intent);
        }

    }


}