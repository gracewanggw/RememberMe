package com.example.remembermeryan.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FramilyDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "framily";
    public static final String ID = "id";
    public static final String NAME_FIRST = "first_name";
    public static final String NAME_LAST = "last_name";
    public static final String RELATIONSHIP = "relationship";
    public static final String AGE = "age";
    public static final String BIRTHDAY = "birthday";
    public static final String LOCATION = "location";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String MEMORIES = "memories";
    public static final String IMAGE = "image";

    private static final String DATABASE_NAME = "framilyMembers.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_FIRST + " TEXT, " +
            NAME_LAST + " TEXT, " +
            RELATIONSHIP + " TEXT, " +
            AGE + " INTEGER NOT NULL, " +
            BIRTHDAY + " TEXT, " +
            LOCATION + " TEXT, " +
            PHONE_NUMBER + " TEXT, " +
            IMAGE + " BLOB, " +
            MEMORIES + " BLOB);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FramilyDbHelper.TABLE_NAME;


    // Constructor
    public FramilyDbHelper(Context context) {
        // DATABASE_NAME is, of course the name of the database, which is defined as a String constant
        // DATABASE_VERSION is the version of database, which is defined as an integer constant
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table schema if not exists
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
