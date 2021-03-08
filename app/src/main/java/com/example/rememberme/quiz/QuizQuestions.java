package com.example.rememberme.quiz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.example.rememberme.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.ui.quiz.QuizFragment;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuizQuestions {

    public ArrayList<String> allRelationships = new ArrayList<String>();
    public ArrayList<String> allAge = new ArrayList<String>();
    public ArrayList<String> allBirthday = new ArrayList<String>();
    public ArrayList<String> allLocation = new ArrayList<String>();
    public ArrayList<String> allFaces = new ArrayList<String>();
    public ArrayList<String> allNameL = new ArrayList<String>();
    public ArrayList<String> allPhone = new ArrayList<String>();
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
    private ArrayList<String> quesTypes = new ArrayList<String>();
    private ArrayList<ArrayList<String>> choices = new ArrayList<ArrayList<String>>();
    private ArrayList<Integer> correctAns = new ArrayList<Integer>();
    private ArrayList<Question> quiz = new ArrayList<Question>();
    private int totalQuestions;


    public String question ,
            op1 ,
            op2 ,
            op3 ,
            op4 ,
            correct_answer,
            quesType;

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
            quesTypes.add(quiz.get(i).getQType());
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
        question = testQuestions.get(num) ;

        op1 = choices.get(num).get(0);
        op2 = choices.get(num).get(1);
        op3 = choices.get(num).get(2) ;
        op4 = choices.get(num).get(3) ;
        correct_answer = choices.get(num).get(correctAns.get(num));
        quesType = quesTypes.get(num);
        total = totalQuestions;
        return this;
    }

    public int makeOptions(String answer, String type, int masterSize){
        ArrayList<String> options = new ArrayList<String>();
        int ind;

        Log.d("question", ""+type);

        String ans = "";
        boolean firstpass = true;
        while (options.size() < 3) {
            switch (type) {
                case "relationship":
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allRelationships.size());
                        ans = allRelationships.get(index);
                        if(ans.length() < 1 || ans == null || ans.equals("0")){
                            ans = genRelationship();
                        }
                    } else {
                        ans = genRelationship();
                    }
                    break;
                case "age":
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allAge.size());
                        ans = allAge.get(index);
                        if(ans.length() < 1 || ans == null || ans.equals("0")){
                            ans = genAge(answer);
                        }
                    } else {
                        ans = genAge(answer);
                    }
                    break;
                case "birthday":
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allBirthday.size());
                        ans = allBirthday.get(index);
                        if(ans.length() < 1 || ans == null || ans.equals("0")){
                            ans = genBirthday(answer);
                        }
                    } else {
                        ans = genBirthday(answer);
                    }
                    break;
                case "location":
                    if (masterSize > 3) {
                        int index = (int) (Math.random() * allLocation.size());
                        ans = allLocation.get(index);
                        if(ans.length() < 1 || ans == null || ans.equals("0")){
                            ans = genLocation();
                        }
                    } else {
                        ans = genLocation();
                    }
                    break;
                case "photo":
                   int index = (int) (Math.random() * allFaces.size());
                    String tempPic = allFaces.get(index);
                    ans = tempPic;
                    break;
                case "lname":
                    if (masterSize > 3) {
                        int indexN = (int) (Math.random() * allNameL.size());
                        ans = allNameL.get(indexN);
                        if(ans.length() < 1 || ans == null || ans.equals("0")){
                            ans = genLName();
                        }
                    } else {
                        ans = genLName();
                    }
                    break;
                case "phone":
                    if(firstpass){
                        if(answer.length() != 10){
                            answer = answer.substring(0,10);
                        }
                        answer = answer.substring(0,3)+"-"+answer.substring(3,6)+"-"+answer.substring(6);
                        firstpass = false;
                    }
                   String temp = genPhone(answer);
                    ans = temp.substring(0,3)+"-"+temp.substring(3,6)+"-"+temp.substring(6);
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

        Log.d("question choices made", ""+choices);

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
        int num  = (int)(Math.random() * 10 + (Integer.parseInt(ref)-5));
        return ""+num;
    }

    public String genPhone(String refer){

        String ref = refer.substring(0,3) + refer.substring(4,7) + refer.substring(8);
        String finalGen = "";
        int num  = (int)(Math.random() * ref.length());
        int change = Character.getNumericValue(ref.charAt(num));
        String newNum = getRandomElement(new String[]{""+((change+1)%10),
                ""+((change-1)%10),
                ""+((change-2)%10),
                ""+((change+2)%10)});
        if(num != ref.length()-1){
          finalGen = ref.substring(0, num) + newNum + ref.substring(num+1);
        } else{
          finalGen = ref.substring(0, num) + newNum;
        }
        return finalGen;
    }

    public String genLName(){
        Resources r = context.getResources();
        String names[] = r.getStringArray(R.array.surnames);
        return getRandomElement(names);
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
        allFaces = getValidFaces();
        allNameL = dataSource.fetchFramilyColumn("last_name");
        allPhone = dataSource.fetchFramilyColumn("phone_number");
    }

    public void updateMasterLists(){
        new Thread() {
            @Override
            public void run() {
                allRelationships = dataSource.fetchFramilyColumn("relationship");
                allAge = dataSource.fetchFramilyColumn("age");
                allLocation = dataSource.fetchFramilyColumn("location");
                allBirthday = dataSource.fetchFramilyColumn("birthday");
                allFaces = getValidFaces();
                allNameL = dataSource.fetchFramilyColumn("last_name");
                allPhone = dataSource.fetchFramilyColumn("phone_number");
            }
        }.start();
    }

    public ArrayList<String> getValidFaces(){
        ArrayList<String> realImages = new  ArrayList<String>();
        ArrayList<String> allImgs = dataSource.fetchFramilyColumn("photo");

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable._pic);
        Drawable defaultPic = new BitmapDrawable(context.getResources(), bitmap);
        Drawable.ConstantState cdefault = defaultPic.getConstantState();

        for(String img : allImgs) {
            try {
                FileInputStream fis = context.openFileInput(img);
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                Drawable d = new BitmapDrawable(context.getResources(), bmap);
                Drawable.ConstantState c1 = d.getConstantState();

                if(!c1.equals(cdefault)){
                    realImages.add(img);
                }
            } catch (IOException e) {
            }
        }

        return realImages;
    }

}
