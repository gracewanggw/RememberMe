package com.example.rememberme.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RememberMeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rememberMe.db";
    private static final int DATABASE_VERSION = 17;


    public static final String TABLE_NAME_FRAMILY = "framily";
    public static final String ID_FRAMILY = "id";
    public static final String NAME_FIRST = "first_name";
    public static final String NAME_LAST = "last_name";
    public static final String RELATIONSHIP = "relationship";
    public static final String AGE = "age";
    public static final String BIRTHDAY = "birthday";
    public static final String LOCATION = "location";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String MEMORIES = "memories";
    public static final String IMAGE_FRAMILY = "image";
    public static final String PHOTO_FILE = "photo";

    public static final String TABLE_NAME_MEMORIES = "memories";
    public static final String ID_MEMORIES = "id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String IMAGE_MEMORY = "image";
    public static final String AUDIO = "audio";
    public static final String MEMORY_FILE = "memory";

    public static final String TABLE_NAME_QUIZ = "quiz";
    public static final String ID_QUIZ = "id";
    public static final String PERSON = "name";
    //ie/ birthday, face, misc?
    public static final String QUESTION_TYPE = "categoryquesiton";
    public static final String DATA_TYPE_QUESTION = "typequestion"; //string, image, audio
    public static final String QUESTION_STRUCTURE = "formatquestion";
    public static final String QUESTION = "questiontext";
    public static final String DATA_TYPE_ANSWER = "typeanswer"; //string, image, audio
    public static final String ANSWER_STRUCTURE = "formatanswer";
    public static final String CORRECT_ANSWER = "answer";
    public static final String REVIEW = "toreview";

    private static final String CREATE_FRAMILY_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FRAMILY + "( " +
            ID_FRAMILY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_FIRST + " TEXT, " +
            NAME_LAST + " TEXT, " +
            RELATIONSHIP + " TEXT, " +
            AGE + " INTEGER NOT NULL, " +
            BIRTHDAY + " TEXT, " +
            LOCATION + " TEXT, " +
            PHONE_NUMBER + " TEXT, " +
            IMAGE_FRAMILY + " BLOB, " +
            MEMORIES + " BLOB, " +
            PHOTO_FILE + " TEXT);";

    private static final String CREATE_MEMORY_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MEMORIES + "( " +
            ID_MEMORIES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT, " +
            TEXT + " TEXT, " +
            IMAGE_MEMORY + " BLOB, " +
            AUDIO + " TEXT, " +
            MEMORY_FILE + " TEXT);";

    private static final String CREATE_QUIZ_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_QUIZ + "( " +
            ID_QUIZ + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PERSON + " TEXT, " +
            QUESTION_TYPE + " TEXT, " +
            DATA_TYPE_QUESTION + " TEXT, " +
            QUESTION_STRUCTURE + " TEXT, " +
            QUESTION + " BLOB, " +
            DATA_TYPE_ANSWER + " TEXT, " +
            ANSWER_STRUCTURE + " TEXT, " +
            CORRECT_ANSWER + " BLOB, " +
            REVIEW + " BOOLEAN);";

    // Constructor
    public RememberMeDbHelper(Context context) {
        // DATABASE_NAME is, of course the name of the database, which is defined as a String constant
        // DATABASE_VERSION is the version of database, which is defined as an integer constant
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table schema if not exists
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FRAMILY_ENTRIES);
        db.execSQL(CREATE_MEMORY_ENTRIES);
        db.execSQL(CREATE_QUIZ_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + RememberMeDbHelper.TABLE_NAME_FRAMILY);
        db.execSQL("DROP TABLE IF EXISTS " + RememberMeDbHelper.TABLE_NAME_MEMORIES);
        db.execSQL("DROP TABLE IF EXISTS " + RememberMeDbHelper.TABLE_NAME_QUIZ);
        onCreate(db);
    }

}
