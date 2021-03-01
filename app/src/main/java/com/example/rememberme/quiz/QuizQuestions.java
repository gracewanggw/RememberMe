package com.example.rememberme.quiz;

import java.util.ArrayList;

public class QuizQuestions {

    private int totalQuestions = 4;

    private String testQuestions[] = {
            "What is the color of the sky?",
            "What term is it right now",
            "If you had to choose between never eating sugar again or never eating salt again what is the right choice?",
            "What is the best state in the US?"
    };

    private String choices[][] = {
            {"Blue", "Red", "Pink", "Green"},
            {"20W", "16F", "21W", "21S"},
            {"Never eating sugar", "Never eating salt", "I refuse", "Both, I hate sugar and salt"},
            {"New Hampshire", "California", "Texas", "Oregon"}
    };

    private int correctAns[] = {0,2,2,3};

    public String question ,
            op1 ,
            op2 ,
            op3 ,
            op4 ,
            correct_answer;

    public int total;

    public int getSize(){
        return testQuestions.length;
    }

    public ArrayList<String> getAllQuestions() {
        ArrayList<String> ret = new ArrayList<String>();
        for (String text : testQuestions) {
            ret.add(text);
        }
        return ret;
    }
    public ArrayList<String> getAnswers(){
        ArrayList<String> ret = new ArrayList<String>();
        int i = 0;
        for (int ans : correctAns) {
            ret.add(choices[i][ans]);
            i++;
        }
        return ret;
    }

    public QuizQuestions getQuestion(int num){
        question = testQuestions[num] ;
        op1 = choices[num][0];
        op2 = choices[num][1];
        op3 = choices[num][2] ;
        op4 = choices[num][3] ;
        correct_answer = choices[num][correctAns[num]];
        total = totalQuestions;
        return this;
    }
}
