package com.example.rememberme.quiz;

import androidx.appcompat.app.AppCompatActivity;
import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.ui.quiz.QuizFragment;

import java.util.ArrayList;

public class QuizQuestions extends AppCompatActivity {

    public ArrayList<String> allRelationships = new ArrayList<String>();
    public ArrayList<String> allAge = new ArrayList<String>();
    public ArrayList<String> allBirthday = new ArrayList<String>();
    public ArrayList<String> allLocation = new ArrayList<String>();
    RememberMeDbSource dataSource = new RememberMeDbSource(this.getApplicationContext());


//    private int totalQuestions = 4;
//
//    private String testQuestions[] = {
//            "What is the color of the sky?",
//            "What term is it right now",
//            "If you had to choose between never eating sugar again or never eating salt again what is the right choice?",
//            "What is the best state in the US?"
//    };
//
//    private String choices[][] = {
//            {"Blue", "Red", "Pink", "Green"},
//            {"20W", "16F", "21W", "21S"},
//            {"Never eating sugar", "Never eating salt", "I refuse", "Both, I hate sugar and salt"},
//            {"New Hampshire", "California", "Texas", "Oregon"}
//    };
//
//    private int correctAns[] = {0,2,2,3};

    private ArrayList<String> testQuestions = new ArrayList<String>();
    private ArrayList<ArrayList<String>> choices = new ArrayList<ArrayList<String>>();
    private ArrayList<Integer> correctAns = new ArrayList<Integer>();
    private ArrayList<Question> quiz = new ArrayList<Question>();
    private int totalQuestions = 10;


    public String question ,
            op1 ,
            op2 ,
            op3 ,
            op4 ,
            correct_answer;

    public int total;

    public QuizQuestions(ArrayList<Question> quiz){
        this.quiz = quiz;
        dataSource.open();
        updateMasterLists();
        for (int i = 0; i < quiz.size(); i++){
            testQuestions.add(quiz.get(i).getmQuestion());
            correctAns.add(makeOptions(quiz.get(i).getAnswer(), quiz.get(i).getQType()));
        }
        dataSource.close();
    }

    public int getSize(){
        return testQuestions.size();
    }
    public Question getQuestionObj(int num){
        return quiz.get(num);
    }


    public QuizQuestions getQuestion(int num){
//        question = testQuestions[num] ;
//        op1 = choices[num][0];
//        op2 = choices[num][1];
//        op3 = choices[num][2] ;
//        op4 = choices[num][3] ;
//        correct_answer = choices[num][correctAns[num]];
        question = testQuestions.get(num) ;
        op1 = choices.get(num).get(0);
        op2 = choices.get(num).get(1);
        op3 = choices.get(num).get(2) ;
        op4 = choices.get(num).get(3) ;
        correct_answer = choices.get(num).get(correctAns.get(num));
        total = totalQuestions;
        return this;
    }

    public int makeOptions(String answer, String type){
        ArrayList<String> options = new ArrayList<String>();
        int ind;
        switch (type) {
            case "relationship":
                while(options.size() < 3){
                    int index = (int)(Math.random() * allRelationships.size());
                    if (!options.contains(allRelationships.get(index))){
                        options.add(allRelationships.get(index));
                    }
                }
                break;
            case "age":
                while(options.size() < 3){
                    int index = (int)(Math.random() * allAge.size());
                    if (!options.contains(allAge.get(index))){
                        options.add(allAge.get(index));
                    }
                }
                break;
            case "birthday":
                while(options.size() < 3){
                    int index = (int)(Math.random() * allBirthday.size());
                    if (!options.contains(allBirthday.get(index))){
                        options.add(allBirthday.get(index));
                    }
                }
                break;
            case "location":
                while(options.size() < 3){
                    int index = (int)(Math.random() * allLocation.size());
                    if (!options.contains(allLocation.get(index))){
                        options.add(allLocation.get(index));
                    }
                }
                break;
        }
        ind = (int)(Math.random() * 4);
        if (ind == 3) {
            options.add(answer);
        }else{
            options.add(ind, answer);
        }

        choices.add(options);
        return ind;
    }

    public void updateMasterLists(){
        new Thread() {
            @Override
            public void run() {
                allRelationships = dataSource.fetchFramilyColumn("relationship");
                allAge = dataSource.fetchFramilyColumn("age");
                allLocation = dataSource.fetchFramilyColumn("location");
                allBirthday = dataSource.fetchFramilyColumn("birthday");
            }
        }.start();
    }

}
