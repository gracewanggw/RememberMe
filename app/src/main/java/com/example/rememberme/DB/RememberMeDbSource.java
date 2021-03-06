package com.example.rememberme.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.amitshekhar.DebugDB;
import com.example.rememberme.AddEditMemoryActivity;
import com.example.rememberme.Framily;
import com.example.rememberme.Memory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RememberMeDbSource {

    private SQLiteDatabase database;
    private RememberMeDbHelper dbHelper;
    private String[] framilyColumns = { RememberMeDbHelper.ID_FRAMILY, RememberMeDbHelper.NAME_FIRST,
            RememberMeDbHelper.NAME_LAST, RememberMeDbHelper.RELATIONSHIP, RememberMeDbHelper.AGE,
            RememberMeDbHelper.BIRTHDAY, RememberMeDbHelper.LOCATION, RememberMeDbHelper.PHONE_NUMBER,
            RememberMeDbHelper.MEMORIES, RememberMeDbHelper.IMAGE_FRAMILY, RememberMeDbHelper.PHOTO_FILE};

    private String[] memoryColumns = { RememberMeDbHelper.ID_MEMORIES,RememberMeDbHelper.TITLE,
            RememberMeDbHelper.TEXT, RememberMeDbHelper.IMAGE_MEMORY, RememberMeDbHelper.AUDIO};

    private static final String TAG = "rdudak";

    public RememberMeDbSource(Context context) {
        dbHelper = new RememberMeDbHelper(context);
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
        values.put(RememberMeDbHelper.ID_FRAMILY, framily.getId());
        values.put(RememberMeDbHelper.NAME_FIRST, framily.getNameFirst());
        values.put(RememberMeDbHelper.NAME_LAST, framily.getNameLast());
        values.put(RememberMeDbHelper.RELATIONSHIP, framily.getRelationship());
        values.put(RememberMeDbHelper.AGE, framily.getAge());
        values.put(RememberMeDbHelper.BIRTHDAY, framily.getBirthday());
        values.put(RememberMeDbHelper.LOCATION, framily.getLocation());
        values.put(RememberMeDbHelper.PHONE_NUMBER, framily.getPhoneNumber());
        values.put(RememberMeDbHelper.IMAGE_FRAMILY, framily.getImage());
        values.put(RememberMeDbHelper.PHOTO_FILE, framily.getPhotoFileName());
        if (!framily.getMemories().isEmpty())
            values.put(RememberMeDbHelper.MEMORIES, arrayListToByteArray(framily.getMemories()));
        long insertId = database.insert(RememberMeDbHelper.TABLE_NAME_FRAMILY, null, values);
        Cursor cursor = database.query(RememberMeDbHelper.TABLE_NAME_FRAMILY,
                framilyColumns,
                RememberMeDbHelper.ID_FRAMILY + " = " + insertId,
                null,null, null, null);
        cursor.moveToFirst();//now the cursor has only one element but the index is -1, so we need to do cursor.moveToFirst()
        Framily newEntry = cursorToFramily(cursor);
        Log.d(TAG, "Name = " + newEntry.getNameFirst() + " " + newEntry.getNameLast() + " insert ID = " + newEntry.getId());
        cursor.close();
        return insertId;
    }

    public long insertMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(RememberMeDbHelper.ID_MEMORIES, memory.getId());
        values.put(RememberMeDbHelper.TITLE, memory.getTitle());
        values.put(RememberMeDbHelper.TEXT, memory.getText());
        values.put(RememberMeDbHelper.IMAGE_MEMORY, memory.getImage());
        values.put(RememberMeDbHelper.AUDIO, memory.getAudio());

        long insertId = database.insert(RememberMeDbHelper.TABLE_NAME_MEMORIES, null, values);
        Cursor cursor = database.query(RememberMeDbHelper.TABLE_NAME_MEMORIES,
                memoryColumns,
                RememberMeDbHelper.ID_MEMORIES + " = " + insertId,
                null,null, null, null);
        cursor.moveToFirst();//now the cursor has only one element but the index is -1, so we need to do cursor.moveToFirst()
        Memory newEntry = cursorToMemory(cursor);
        Log.d(TAG, "Name = " + newEntry.getTitle() + " - " + newEntry.getText() + " insert ID = " + newEntry.getId());
        cursor.close();
        return insertId;
    }

    // Remove an entry by giving its index
    public void removeFramily(Long rowIndex) {
        Log.d("rdudak", DebugDB.getAddressLog());
        database.delete(RememberMeDbHelper.TABLE_NAME_FRAMILY, RememberMeDbHelper.ID_FRAMILY
                + " = " + rowIndex, null);
    }

    // Remove an entry by giving its index
    public void removeMemory(Long rowIndex) {
        Log.d("rdudak", DebugDB.getAddressLog());
        database.delete(RememberMeDbHelper.TABLE_NAME_MEMORIES, RememberMeDbHelper.ID_MEMORIES
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public Framily fetchFramilyByIndex(Long rowId) {
        Log.d("rdudak", DebugDB.getAddressLog());
        Cursor cursor = database.query(RememberMeDbHelper.TABLE_NAME_FRAMILY, framilyColumns,
                RememberMeDbHelper.ID_FRAMILY + " = " + rowId, null,
                null, null, null);
        Framily entry = new Framily();
        if (cursor != null && cursor.moveToFirst()) {
            entry.setId(cursor.getLong(cursor.getColumnIndex(RememberMeDbHelper.ID_FRAMILY)));
            entry.setNameFirst(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.NAME_FIRST)));
            entry.setNameLast(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.NAME_LAST)));
            entry.setRelationship(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.RELATIONSHIP)));
            entry.setAge(cursor.getInt(cursor.getColumnIndex(RememberMeDbHelper.AGE)));
            entry.setBirthday(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.BIRTHDAY)));
            entry.setLocation(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.LOCATION)));
            entry.setPhoneNumber(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.PHONE_NUMBER)));
            entry.setMemories(byteArrayToArrayList(cursor.getBlob(cursor.getColumnIndex(RememberMeDbHelper.MEMORIES))));
            entry.setImage(cursor.getBlob(cursor.getColumnIndex(RememberMeDbHelper.IMAGE_FRAMILY)));
            entry.setPhotoFileName(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.PHOTO_FILE)));
            cursor.close();
        }
        return entry;
    }

    // Query a specific entry by its index.
    public Memory fetchMemoryByIndex(Long rowId) {
        Log.d("rdudak", DebugDB.getAddressLog());
        Cursor cursor = database.query(RememberMeDbHelper.TABLE_NAME_MEMORIES, memoryColumns,
                RememberMeDbHelper.ID_MEMORIES + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        Memory entry = new Memory();
        entry.setId(cursor.getLong(cursor.getColumnIndex(RememberMeDbHelper.ID_MEMORIES)));
        entry.setTitle(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.TITLE)));
        entry.setText(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.TEXT)));
        entry.setImage(cursor.getBlob(cursor.getColumnIndex(RememberMeDbHelper.IMAGE_MEMORY)));
        entry.setAudio(cursor.getString(cursor.getColumnIndex(RememberMeDbHelper.AUDIO)));

        cursor.close();
        return entry;
    }

    //Fetches the most recent entry
    public Framily fetchLastFramilyEntry() {
        String selectQuery = "SELECT  * FROM " + "framily";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToLast();
        Framily framily = cursorToFramily(cursor);
        return framily;
    }

    //Fetches the most recent entry
    public Memory fetchLastMemoryEntry() {
        String selectQuery = "SELECT  * FROM " + "memories";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToLast();
        Memory memory = cursorToMemory(cursor);
        return memory;
    }

    // Query the entire table, return all rows
    public List<Framily> fetchFramilyEntries() {
        List<Framily> entries = new ArrayList<Framily>();
        Cursor cursor = database.query(RememberMeDbHelper.TABLE_NAME_FRAMILY,
                framilyColumns, null, null, null, null, null);
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

    // Query the entire table, return all rows
    public List<Memory> fetchMemoryEntries() {
        List<Memory> entries = new ArrayList<Memory>();
        Cursor cursor = database.query(RememberMeDbHelper.TABLE_NAME_MEMORIES,
                memoryColumns, null, null, null, null, null);
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

    public void updateFramilyEntry(Long rowId, Framily framily) {
        ContentValues values = new ContentValues();
        values.put(RememberMeDbHelper.ID_FRAMILY, framily.getId());
        values.put(RememberMeDbHelper.NAME_FIRST, framily.getNameFirst());
        values.put(RememberMeDbHelper.NAME_LAST, framily.getNameLast());
        values.put(RememberMeDbHelper.RELATIONSHIP, framily.getRelationship());
        values.put(RememberMeDbHelper.AGE, framily.getAge());
        values.put(RememberMeDbHelper.BIRTHDAY, framily.getBirthday());
        values.put(RememberMeDbHelper.LOCATION, framily.getLocation());
        values.put(RememberMeDbHelper.PHONE_NUMBER, framily.getPhoneNumber());
        values.put(RememberMeDbHelper.IMAGE_FRAMILY, framily.getImage());
        values.put(RememberMeDbHelper.PHOTO_FILE, framily.getPhotoFileName());
        if (!framily.getMemories().isEmpty())
            values.put(RememberMeDbHelper.MEMORIES, arrayListToByteArray(framily.getMemories()));
        database.update(RememberMeDbHelper.TABLE_NAME_FRAMILY, values, RememberMeDbHelper.ID_FRAMILY + " = " + rowId, null);
    }

    public void updateMemoryEntry(Long rowId) {
        ContentValues values = new ContentValues();
        Memory memory = AddEditMemoryActivity.memory;
        values.put(RememberMeDbHelper.ID_MEMORIES, memory.getId());
        values.put(RememberMeDbHelper.TITLE, memory.getTitle());
        values.put(RememberMeDbHelper.TEXT, memory.getText());
        values.put(RememberMeDbHelper.IMAGE_MEMORY, memory.getImage());
        values.put(RememberMeDbHelper.AUDIO, memory.getAudio());
        database.update(RememberMeDbHelper.TABLE_NAME_MEMORIES, values, RememberMeDbHelper.ID_MEMORIES + " = " + rowId, null);
    }

    private Framily cursorToFramily(Cursor cursor) {
        Framily framily = new Framily();
        framily.setId(cursor.getLong(0));
        framily.setNameFirst(cursor.getString(1));
        framily.setNameLast(cursor.getString(2));
        framily.setRelationship(cursor.getString(3));
        framily.setAge(cursor.getInt(4));
        framily.setBirthday(cursor.getString(5));
        framily.setLocation(cursor.getString(6));
        framily.setPhoneNumber(cursor.getString(7));
        framily.setImage(cursor.getBlob(8));
        if (cursor.getBlob(9) != null)
            framily.setMemories(byteArrayToArrayList(cursor.getBlob(9)));
        return framily;
    }

    private Memory cursorToMemory(Cursor cursor) {
        Memory memory = new Memory();
        memory.setId(cursor.getLong(0));
        memory.setTitle(cursor.getString(1));
        memory.setText(cursor.getString(2));
        memory.setImage(cursor.getBlob(3));
        memory.setAudio(cursor.getString(4));
        return memory;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
        else return null;
    }

    public Bitmap byteArrayToBitmap(byte[] array) {
        if (array != null)
            return BitmapFactory.decodeByteArray(array, 0, array.length);
        else return null;
    }


    public byte[] arrayListToByteArray(ArrayList<Long> memories) {
        if (memories.isEmpty()) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        byte[] bytes = new byte[memories.size()];
        try {
            for (Long element : memories) {
                out.writeUTF(element.toString());
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public ArrayList<Long> byteArrayToArrayList(byte[] memoryBytes) {
        ArrayList<Long> memories = new ArrayList<Long>();
        if (memoryBytes != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(memoryBytes);
            DataInputStream in = new DataInputStream(bais);
            try {
                while (in.available() > 0) {
                    String memory = in.readUTF();
                    memories.add(Long.parseLong(memory));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return memories;
    }

    public byte[] fileToByteArray(File file) {
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            for (int readNum; (readNum = fis.read(b)) != -1; ) {
                bos.write(b, 0, readNum);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File byteArrayToFile(byte[] fileBytes) {
        File file = new File("audio_file.3gp");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(fileBytes);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
