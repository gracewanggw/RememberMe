package com.example.rememberme.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amitshekhar.DebugDB;
import com.example.rememberme.AddMemoryActivity;
import com.example.rememberme.EditFramilyProfile;
import com.example.rememberme.Framily;
import com.example.rememberme.Memory;

import java.util.ArrayList;
import java.util.List;

public class MemoriesDbSource {

    private SQLiteDatabase database;
    private MemoriesDbHelper dbHelper;
    private String[] allColumns = { MemoriesDbHelper.ID,MemoriesDbHelper.TITLE,
            MemoriesDbHelper.TEXT, MemoriesDbHelper.IMAGE, /*MemoriesDbHelper.AUDIO*/};

    private static final String TAG = "rdudak";

    public MemoriesDbSource(Context context) {
        dbHelper = new MemoriesDbHelper(context);
    }

    public void open() throws SQLException {
        //XD: getWritableDatabase() - A SQLiteOpenHelper method. It creates and/or opens a database
        // that will be used for reading and writing.
        //XD: The first time this is called, the database will be opened and onCreate(SQLiteDatabase),
        // onUpgrade(SQLiteDatabase, int, int) and/or onOpen(SQLiteDatabase) will be called.
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(MemoriesDbHelper.ID, memory.getId());
        values.put(MemoriesDbHelper.TITLE, memory.getTitle());
        values.put(MemoriesDbHelper.TEXT, memory.getText());
        values.put(MemoriesDbHelper.IMAGE, memory.getImage());
       // values.put(MemoriesDbHelper.AUDIO, memory.getAudio());

        long insertId = database.insert(FramilyDbHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(FramilyDbHelper.TABLE_NAME,
                allColumns,
                MemoriesDbHelper.ID + " = " + insertId,
                null,null, null, null);
        cursor.moveToFirst();//now the cursor has only one element but the index is -1, so we need to do cursor.moveToFirst()
        Memory newEntry = cursorToMemory(cursor);
        Log.d(TAG, "Name = " + newEntry.getTitle() + " - " + newEntry.getText() + " insert ID = " + newEntry.getId());
        cursor.close();
        return insertId;
    }

    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
        Log.d("rdudak", DebugDB.getAddressLog());
        database.delete(FramilyDbHelper.TABLE_NAME, MemoriesDbHelper.ID
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public Memory fetchEntryByIndex(int rowId) {
        Log.d("rdudak", DebugDB.getAddressLog());
        Cursor cursor = database.query(MemoriesDbHelper.TABLE_NAME, allColumns,
                MemoriesDbHelper.ID + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        Memory entry = new Memory();
        entry.setId(cursor.getInt(cursor.getColumnIndex(MemoriesDbHelper.ID)));
        entry.setTitle(cursor.getString(cursor.getColumnIndex(MemoriesDbHelper.TITLE)));
        entry.setText(cursor.getString(cursor.getColumnIndex(MemoriesDbHelper.TEXT)));
        entry.setImage(cursor.getString(cursor.getColumnIndex(MemoriesDbHelper.IMAGE)));
       // entry.setAudio(cursor.getInt(cursor.getColumnIndex(MemoriesDbHelper.AUDIO)));

        cursor.close();
        return entry;
    }

    //Fetches the most recent entry
    public Memory fetchLastEntry() {
        String selectQuery = "SELECT  * FROM " + "sqlite_sequence";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToLast();
        Memory memory = cursorToMemory(cursor);
        return memory;
    }

    // Query the entire table, return all rows
    public List<Memory> fetchEntries() {
        List<Memory> entries = new ArrayList<Memory>();
        Cursor cursor = database.query(MemoriesDbHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst(); //Move the cursor to the first row.
        while (!cursor.isAfterLast()) {//Returns whether the cursor is pointing to the position after the last row.
            Memory entry = cursorToMemory(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    public void updateEntry(int rowId) {
        ContentValues values = new ContentValues();
        Memory memory = AddMemoryActivity.memory;
        values.put(MemoriesDbHelper.ID, memory.getId());
        values.put(MemoriesDbHelper.TITLE, memory.getTitle());
        values.put(MemoriesDbHelper.TEXT, memory.getText());
        values.put(MemoriesDbHelper.IMAGE, memory.getImage());
       // values.put(MemoriesDbHelper.AUDIO, memory.getAudio());
        database.update(MemoriesDbHelper.TABLE_NAME, values, MemoriesDbHelper.ID + " = " + rowId, null);
    }

    private Memory cursorToMemory(Cursor cursor) {
        Memory memory = new Memory();
        memory.setId(cursor.getInt(cursor.getColumnIndex(MemoriesDbHelper.ID)));
        memory.setTitle(cursor.getString(cursor.getColumnIndex(MemoriesDbHelper.TITLE)));
        memory.setText(cursor.getString(cursor.getColumnIndex(MemoriesDbHelper.TEXT)));
        memory.setImage(cursor.getString(cursor.getColumnIndex(MemoriesDbHelper.IMAGE)));
       // memory.setAudio(cursor.getInt(cursor.getColumnIndex(MemoriesDbHelper.AUDIO)));
        return memory;
    }
}
