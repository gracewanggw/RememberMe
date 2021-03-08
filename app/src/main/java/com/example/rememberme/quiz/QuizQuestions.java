package com.example.rememberme.quiz;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.example.rememberme.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.ui.quiz.QuizFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuizQuestions {

    public ArrayList<String> allRelationships = new ArrayList<String>();
    public ArrayList<String> allAge = new ArrayList<String>();
    public ArrayList<String> allBirthday = new ArrayList<String>();
    public ArrayList<String> allLocation = new ArrayList<String>();
    RememberMeDbSource dataSource;


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
    private int totalQuestions;


    public String question ,
            op1 ,
            op2 ,
            op3 ,
            op4 ,
            correct_answer;

    public int total;
    Context context;

    public QuizQuestions(ArrayList<Question> quiz, Context context){
        this.quiz = quiz;
        totalQuestions = quiz.size();
        dataSource = new RememberMeDbSource(context);
        this.context = context;
        dataSource.open();

        if(allAge.size() == 0) {
            initMasterLists();
        }else {
            updateMasterLists();
        }

        for (int i = 0; i < quiz.size(); i++){
            testQuestions.add(quiz.get(i).getmQuestion());
            correctAns.add(makeOptions(quiz.get(i).getAnswer(), quiz.get(i).getQType(), quiz.size()));
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

    public int makeOptions(String answer, String type, int masterSize){
        ArrayList<String> options = new ArrayList<String>();
        int ind;

        Log.d("question", ""+type);

        String ans = "";
        while (options.size() < 3) {
            switch (type) {
                case "relationship":
                    Log.d("question", "in rel");
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allRelationships.size());
                        ans = allRelationships.get(index);
                    } else {
                        ans = genRelationship();
                    }
                    break;
                case "age":
                    Log.d("question", "in a");
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allAge.size());
                        ans = allAge.get(index);
                    } else {
                        ans = genAge(answer);
                    }
                    break;
                case "birthday":
                    Log.d("question", "in b");
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allBirthday.size());
                        ans = allBirthday.get(index);
                    } else {
                        ans = genBirthday(answer);
                    }
                    break;
                case "location":
                    Log.d("question", "in loc");
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allLocation.size());
                        ans = allLocation.get(index);
                    } else {
                        ans = genLocation();
                    }
                    break;

            }

            if (!ans.equals(answer) && !options.contains(ans)) {
                options.add(ans);
            }
        }



        ind = (int)(Math.random() * 4);
        if (ind == 3) {
            options.add(answer);
        }else{
            options.add(ind, answer);
        }

        choices.add(options);

        Log.d("question1", ""+choices);
        return ind;
    }

    public String genBirthday(String ref){

        String genDate = "";

        //month
        String months[] = {"January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        Log.d("df", ref);

        String[] splited = ref.split(" ");
        Log.d("df", ""+splited[0]);
        Log.d("df", ""+splited[1]);
        Log.d("df", ""+splited[2]);

        int monthInd=0;
        for(int i = 0; i < months.length; i++) {
            if(months[i].equals(splited[0])) {
                monthInd = i;
                break;
            }
        }

        if(monthInd == 0){
            genDate += getRandomElement(Arrays.copyOfRange(months, 0, 3));
        }else if(monthInd == 11){
            genDate += getRandomElement(Arrays.copyOfRange(months, 9, 12));
        }
        else{
            genDate += getRandomElement(Arrays.copyOfRange(months, monthInd - 1, monthInd + 2));
        }

        genDate += ' ';

        //date
        if(genDate.equals("February")){
            int num = 0;
            while(num < 1 || num > 28){
                num = (int)(Math.random() * 10 + (Integer.parseInt(splited[1])-5));
            }
            genDate += ""+num;
        }
        else{
            int num = 0;
            while(num < 1 || num > 30){
                num = (int)(Math.random() * 10 + (Integer.parseInt(splited[1])-5));
            }
            genDate += ""+num;
        }

        genDate += ' ';

        //year
        int year = Integer.parseInt(splited[2]);
        String years[] = {splited[2], String.valueOf(year-1), String.valueOf(year+1)};
        genDate += getRandomElement(years);

        return genDate;
    }

    public String genRelationship(){
        //word to vec later?

        String relations[] = {"Wife", "Daughter", "Son", "Mother",
                "Father", "Aunt", "Uncle", "Grandson",
                "Granddaughter", "Friend", "Best Friend", "Colleague",
                "Neighbor", "Son-in-Law", "Daughter-in-Law", "Pet",
                "Cousin", "Niece", "Nephew", "Distant Relative"
        };
        return getRandomElement(relations);

    }

    public String genLocation(){
        Resources r = context.getResources();
        String cities[] = r.getStringArray(R.array.locations_cities);
        return getRandomElement(cities);

    }

    public String genAge(String ref){
        Log.d("dani", ref);
        int num  = (int)(Math.random() * 10 + (Integer.parseInt(ref)-5));
        Log.d("dani", ""+num);
        return ""+num;
    }


    public String getRandomElement(String[] list)
    {
        Random rand = new Random();
        int randomIndex = rand.nextInt(list.length);
        return list[randomIndex];
    }

    public void initMasterLists(){
        allRelationships = dataSource.fetchFramilyColumn("relationship");
        allAge = dataSource.fetchFramilyColumn("age");
        allLocation = dataSource.fetchFramilyColumn("location");
        allBirthday = dataSource.fetchFramilyColumn("birthday");
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
