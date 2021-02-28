package com.example.rememberme.ui.quiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rememberme.R;
import com.example.rememberme.quiz.Quiz;

public class QuizFragment extends Fragment{

    private DashboardViewModel dashboardViewModel;

    LinearLayout quizAll;
    LinearLayout nameFace;
    LinearLayout birthdays;
    LinearLayout review;

    Button fill_in_blank;
    Button mult_choice;

    boolean fib = true;

    public static final String QUIZ_TYPE_KEY = "quiz type";
    public static final int QUIZ_TYPE_ALL_KEY = 0;
    public static final int QUIZ_TYPE_PERSON_KEY = 1;
    public static final int QUIZ_TYPE_REVIEW_KEY = 2;

    public static final String FILL_IN_BLANK = "fill in blank";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        quizAll = root.findViewById(R.id.quiz_all);
        nameFace = root.findViewById(R.id.name_to_face);
        birthdays = root.findViewById(R.id.quiz_birthdays);
        review = root.findViewById(R.id.quiz_review);
        fill_in_blank = root.findViewById(R.id.fill_in_blank);
        mult_choice = root.findViewById(R.id.mc_button);

        fill_in_blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fib = true;
                mult_choice.setBackgroundColor(Color.TRANSPARENT);
                fill_in_blank.setBackgroundResource(R.drawable.quiz_button1);
                fill_in_blank.setTextColor(Color.WHITE);
                mult_choice.setTextColor(Color.BLACK);
            }
        });

        mult_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fib = false;
                mult_choice.setBackgroundResource(R.drawable.quiz_button1);
                fill_in_blank.setBackgroundColor(Color.TRANSPARENT);
                fill_in_blank.setTextColor(Color.BLACK);
                mult_choice.setTextColor(Color.WHITE);
            }
        });

        quizAll.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        quizAll.setAlpha((float)0.4);
                        v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        quizAll.setAlpha((float)1);
                        Intent intent = new Intent(getContext(), Quiz.class);
                        intent.putExtra(FILL_IN_BLANK, fib);
                        intent.putExtra(QUIZ_TYPE_KEY, QUIZ_TYPE_ALL_KEY);
                        startActivityForResult(intent, 0);
                        break;
                    default:
                        break;
                }
                return true;
            }



        });

        nameFace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    nameFace.setAlpha((float)0.4);
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    nameFace.setAlpha((float)1);
//                Intent intent = new Intent(getContext(),QuizActivity.class);
//                intent.putExtra(FILL_IN_BLANK,fib);
//                intent.putExtra(QUIZ_TYPE_KEY,"name to face");
                }
                return true;
            }
        });

        birthdays.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    birthdays.setAlpha((float)0.4);
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    birthdays.setAlpha((float)1);
//                Intent intent = new Intent(getContext(),QuizActivity.class);
//                intent.putExtra(FILL_IN_BLANK,fib);
//                intent.putExtra(QUIZ_TYPE_KEY,"birthdays");
                }
                return true;
            }
        });

        review.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                review.setAlpha((float)0.4);
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                review.setAlpha((float)1);
                MyAlertDialogFragment myDialog = new MyAlertDialogFragment();;
                Bundle bundle = new Bundle();
                bundle.putBoolean("fib", fib);
                bundle.putInt("quizType", QUIZ_TYPE_REVIEW_KEY);
                bundle.putString("title", "Review Mode");
                myDialog.setArguments(bundle);
                myDialog.show(getFragmentManager(), "dialog");
            }
            return true;
            }
        });

        return root;
}

//    @Override
//    public void onFlashClick(){
////        Intent intent = new Intent(getContext(), Flashcard.class);
////        startActivity(intent);
//        Log.d("click", "on flashclick");
//
//    }
//
//    @Override
//    public void onQuizClick() {
//        Intent intent = new Intent(getContext(), Quiz.class);
//        intent.putExtra(FILL_IN_BLANK, fib);
//        intent.putExtra(QUIZ_TYPE_KEY, QUIZ_TYPE_REVIEW_KEY);
//        startActivity(intent);
//    }


}