//package com.example.rememberme.DB;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.ArrayList;
//
//public class QuizDbHelper extends SQLiteOpenHelper {
//
//    public static final String TABLE_NAME = "quiz";
//    public static final String ID = "id";
//    public static final String PERSON = "name";
//    //ie/ birthday, face, misc?
//    public static final String QUESTION_TYPE = "categoryquesiton";
//    public static final String DATA_TYPE_QUESTION = "typequestion"; //string, image, audio
//    public static final String QUESTION_STRUCTURE = "formatquestion";
//    public static final String QUESTION = "questiontext";
//    public static final String DATA_TYPE_ANSWER = "typeanswer"; //string, image, audio
//    public static final String ANSWER_STRUCTURE = "formatanswer";
//    public static final String CORRECT_ANSWER = "answer";
//    public static final String REVIEW = "toreview";
//
//    private static final String DATABASE_NAME = "framily.db";
//    private static final int DATABASE_VERSION = 2;
//
//    private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
//            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            PERSON + " TEXT, " +
//            QUESTION_TYPE + " TEXT, " +
//            DATA_TYPE_QUESTION + " TEXT, " +
//            QUESTION_STRUCTURE + " TEXT, " +
//            QUESTION + " BLOB, " +
//            DATA_TYPE_ANSWER + " TEXT, " +
//            ANSWER_STRUCTURE + " TEXT, " +
//            CORRECT_ANSWER + " BLOB, " +
//            REVIEW + " BOOLEAN);";
//
//    private static final String SQL_DELETE_ENTRIES =
//            "DROP TABLE IF EXISTS " + QuizDbHelper.TABLE_NAME;
//
//
//    // Constructor
//    public QuizDbHelper(Context context) {
//        // DATABASE_NAME is, of course the name of the database, which is defined as a String constant
//        // DATABASE_VERSION is the version of database, which is defined as an integer constant
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    // Create table schema if not exists
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_ENTRIES);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // This database is only a cache for online data, so its upgrade policy is
//        // to simply to discard the data and start over
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
//    }
//
//}
