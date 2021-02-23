package com.example.rememberme.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RememberMeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rememberMe.db";
    private static final int DATABASE_VERSION = 1;

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

    public static final String TABLE_NAME_MEMORIES = "memories";
    public static final String ID_MEMORIES = "id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String IMAGE_MEMORY = "image";
    public static final String AUDIO = "audio";

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
            MEMORIES + " BLOB);";

    private static final String CREATE_MEMORY_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MEMORIES + "( " +
            ID_MEMORIES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT, " +
            TEXT + " TEXT, " +
            IMAGE_MEMORY + " TEXT, " +
            AUDIO + " BLOB);";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + RememberMeDbHelper.TABLE_NAME_FRAMILY);
        db.execSQL("DROP TABLE IF EXISTS " + RememberMeDbHelper.TABLE_NAME_MEMORIES);
        onCreate(db);
    }

}
