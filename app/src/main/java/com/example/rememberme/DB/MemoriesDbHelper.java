package com.example.rememberme.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoriesDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "memories";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    public static final String AUDIO = "audio";

    private static final String DATABASE_NAME = "memories.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT, " +
            TEXT + " TEXT, " +
            IMAGE + " TEXT, " +
            AUDIO + " BLOB);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FramilyDbHelper.TABLE_NAME;


    // Constructor
    public MemoriesDbHelper(Context context) {
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
