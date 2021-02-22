package com.example.rememberme.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rememberme.R;
import com.example.rememberme.quiz.Quiz;

public class QuizFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    Button quizAll;
    Button nameFace;
    Button birthdays;
    Button review;

    Switch fib;

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

        quizAll = root.findViewById(R.id.quiz_all_button);
        nameFace = root.findViewById(R.id.name_to_face_button);
        birthdays = root.findViewById(R.id.quiz_birthday_button);
        review = root.findViewById(R.id.quiz_review_button);
        fib = root.findViewById(R.id.fill_in_blank);

        quizAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Quiz.class);
                intent.putExtra(FILL_IN_BLANK, fib.isSelected());
                intent.putExtra(QUIZ_TYPE_KEY, QUIZ_TYPE_ALL_KEY);
                startActivityForResult(intent, 0);
            }
        });

        nameFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),QuizActivity.class);
//                intent.putExtra(FILL_IN_BLANK,fib.isSelected())
//                intent.putExtra(QUIZ_TYPE_KEY,"name to face");
            }
        });

        birthdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),QuizActivity.class);
//                intent.putExtra(FILL_IN_BLANK,fib.isSelected())
//                intent.putExtra(QUIZ_TYPE_KEY,"birthdays");
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),QuizActivity.class);
//                intent.putExtra(FILL_IN_BLANK,fib.isSelected())
//                intent.putExtra(QUIZ_TYPE_KEY,"review");
            }
        });

        return root;
    }

}