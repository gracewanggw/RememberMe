package com.example.rememberme.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.FrameLayout;

import com.amitshekhar.DebugDB;
import com.example.rememberme.EditFramilyProfile;
import com.example.rememberme.Framily;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FramilyDbSource {

    private SQLiteDatabase database;
    private FramilyDbHelper dbHelper;
    private String[] allColumns = { FramilyDbHelper.ID,FramilyDbHelper.NAME_FIRST,
            FramilyDbHelper.NAME_LAST, FramilyDbHelper.RELATIONSHIP, FramilyDbHelper.AGE,
            FramilyDbHelper.BIRTHDAY, FramilyDbHelper.LOCATION, FramilyDbHelper.PHONE_NUMBER,
            FramilyDbHelper.MEMORIES/*, FramilyDbHelper.IMAGE*/};

    private static final String TAG = "rdudak";

    public FramilyDbSource(Context context) {
        dbHelper = new FramilyDbHelper(context);
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

    public long insertFramily(Framily framily) {
        ContentValues values = new ContentValues();
        values.put(FramilyDbHelper.ID, framily.getId());
        values.put(FramilyDbHelper.NAME_FIRST, framily.getNameFirst());
        values.put(FramilyDbHelper.NAME_LAST, framily.getNameLast());
        values.put(FramilyDbHelper.RELATIONSHIP, framily.getRelationship());
        values.put(FramilyDbHelper.AGE, framily.getAge());
        values.put(FramilyDbHelper.BIRTHDAY, framily.getBirthday());
        values.put(FramilyDbHelper.LOCATION, framily.getLocation());
        values.put(FramilyDbHelper.PHONE_NUMBER, framily.getPhoneNumber());
        if (!framily.getMemories().isEmpty())
            values.put(FramilyDbHelper.MEMORIES, arrayListToByteArray(framily.getMemories()));
//        if (framily.getImage() != null)
//            values.put(FramilyDbHelper.IMAGE, getBitmapAsByteArray(framily.getImage()));
        long insertId = database.insert(FramilyDbHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(FramilyDbHelper.TABLE_NAME,
                allColumns,
                FramilyDbHelper.ID + " = " + insertId,
                null,null, null, null);
        cursor.moveToFirst();//now the cursor has only one element but the index is -1, so we need to do cursor.moveToFirst()
        Framily newEntry = cursorToFramily(cursor);
        Log.d(TAG, "Name = " + newEntry.getNameFirst() + " " + newEntry.getNameLast() + " insert ID = " + newEntry.getId());
        cursor.close();
        return insertId;
    }

    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
        Log.d("rdudak", DebugDB.getAddressLog());
        database.delete(FramilyDbHelper.TABLE_NAME, FramilyDbHelper.ID
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public Framily fetchEntryByIndex(int rowId) {
        Log.d("rdudak", DebugDB.getAddressLog());
        Cursor cursor = database.query(FramilyDbHelper.TABLE_NAME, allColumns,
                FramilyDbHelper.ID + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        Framily entry = new Framily();
        entry.setId(cursor.getInt(cursor.getColumnIndex(FramilyDbHelper.ID)));
        entry.setNameFirst(cursor.getString(cursor.getColumnIndex(FramilyDbHelper.NAME_FIRST)));
        entry.setNameLast(cursor.getString(cursor.getColumnIndex(FramilyDbHelper.NAME_LAST)));
        entry.setRelationship(cursor.getString(cursor.getColumnIndex(FramilyDbHelper.RELATIONSHIP)));
        entry.setAge(cursor.getInt(cursor.getColumnIndex(FramilyDbHelper.AGE)));
        entry.setBirthday(cursor.getString(cursor.getColumnIndex(FramilyDbHelper.BIRTHDAY)));
        entry.setLocation(cursor.getString(cursor.getColumnIndex(FramilyDbHelper.LOCATION)));
        entry.setPhoneNumber(cursor.getString(cursor.getColumnIndex(FramilyDbHelper.PHONE_NUMBER)));
        entry.setMemories(byteArrayToArrayList(cursor.getBlob(cursor.getColumnIndex(FramilyDbHelper.MEMORIES))));
        //entry.setImage(cursor.getInt(cursor.getColumnIndex(FramilyDbHelper.IMAGE)));
        cursor.close();
        return entry;
    }

    //Fetches the most recent entry
    public Framily fetchLastEntry() {
        String selectQuery = "SELECT  * FROM " + "sqlite_sequence";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToLast();
        Framily framily = cursorToFramily(cursor);
        return framily;
    }

    // Query the entire table, return all rows
    public List<Framily> fetchEntries() {
        List<Framily> entries = new ArrayList<Framily>();
        Cursor cursor = database.query(FramilyDbHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst(); //Move the cursor to the first row.
        while (!cursor.isAfterLast()) {//Returns whether the cursor is pointing to the position after the last row.
            Framily entry = cursorToFramily(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    public void updateEntry(int rowId) {
        ContentValues values = new ContentValues();
        Framily framily = EditFramilyProfile.framily;
        values.put(FramilyDbHelper.ID, framily.getId());
        values.put(FramilyDbHelper.NAME_FIRST, framily.getNameFirst());
        values.put(FramilyDbHelper.NAME_LAST, framily.getNameLast());
        values.put(FramilyDbHelper.RELATIONSHIP, framily.getRelationship());
        values.put(FramilyDbHelper.AGE, framily.getAge());
        values.put(FramilyDbHelper.BIRTHDAY, framily.getBirthday());
        values.put(FramilyDbHelper.LOCATION, framily.getLocation());
        values.put(FramilyDbHelper.PHONE_NUMBER, framily.getPhoneNumber());
        if (!framily.getMemories().isEmpty())
            values.put(FramilyDbHelper.MEMORIES, arrayListToByteArray(framily.getMemories()));
//        if (framily.getImage() != null)
//            values.put(FramilyDbHelper.IMAGE, getBitmapAsByteArray(framily.getImage()));
        database.update(FramilyDbHelper.TABLE_NAME, values, "_id=" + rowId, null);
    }

    private Framily cursorToFramily(Cursor cursor) {
        Framily framily = new Framily();
        framily.setId(cursor.getInt(0));
        framily.setNameFirst(cursor.getString(1));
        framily.setNameLast(cursor.getString(2));
        framily.setRelationship(cursor.getString(3));
        framily.setAge(cursor.getInt(4));
        framily.setBirthday(cursor.getString(5));
        framily.setLocation(cursor.getString(6));
        framily.setPhoneNumber(cursor.getString(7));
        if (cursor.getBlob(8) != null)
            framily.setMemories(byteArrayToArrayList(cursor.getBlob(8)));
        return framily;
    }

    public byte[] arrayListToByteArray(ArrayList<Integer> memories) {
        if (memories.isEmpty()) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        byte[] bytes = new byte[memories.size()];
        try {
            for (Integer element : memories) {
                out.writeUTF(element.toString());
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public ArrayList<Integer> byteArrayToArrayList(byte[] memoryBytes) {
        ArrayList<Integer> memories = new ArrayList<Integer>();
        if (memoryBytes != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(memoryBytes);
            DataInputStream in = new DataInputStream(bais);
            try {
                while (in.available() > 0) {
                    String memory = in.readUTF();
                    memories.add(Integer.parseInt(memory));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return memories;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
