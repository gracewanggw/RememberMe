//package com.example.rememberme.DB;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.util.Log;
//import android.widget.FrameLayout;
//
//import com.amitshekhar.DebugDB;
//import com.example.rememberme.quiz.Question;
//import com.example.rememberme.quiz.QuizQuestions;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//public class QuizDbSource {
//
//    private SQLiteDatabase database;
//    private QuizDbHelper dbHelper;
//
//    private String[] allColumns = { QuizDbHelper.ID, QuizDbHelper.PERSON,
//            QuizDbHelper.QUESTION_TYPE, QuizDbHelper.DATA_TYPE_QUESTION, QuizDbHelper.QUESTION_STRUCTURE, QuizDbHelper.QUESTION,
//            QuizDbHelper.DATA_TYPE_ANSWER, QuizDbHelper.ANSWER_STRUCTURE, QuizDbHelper.CORRECT_ANSWER, QuizDbHelper.REVIEW};
//
//
//    public QuizDbSource(Context context) {
//        dbHelper = new QuizDbHelper(context);
//    }
//
//    public void open() throws SQLException {
//        //XD: getWritableDatabase() - A SQLiteOpenHelper method. It creates and/or opens a database
//        // that will be used for reading and writing.
//        //XD: The first time this is called, the database will be opened and onCreate(SQLiteDatabase),
//        // onUpgrade(SQLiteDatabase, int, int) and/or onOpen(SQLiteDatabase) will be called.
//        database = dbHelper.getWritableDatabase();
//    }
//
//    public void close() {
//        dbHelper.close();
//    }
//
//    public long insertQuestion(Question question) {
//        ContentValues values = new ContentValues();
//        values.put(QuizDbHelper.ID, question.getId());
//        values.put(QuizDbHelper.PERSON, question.getPerson());
//        values.put(QuizDbHelper.QUESTION_TYPE, question.getQType());
//        values.put(QuizDbHelper.DATA_TYPE_QUESTION, question.getQDataTypen());
//        values.put(QuizDbHelper.QUESTION_STRUCTURE, question.getQStructure());
//        values.put(QuizDbHelper.QUESTION, question.getmQuestion());
//        values.put(QuizDbHelper.DATA_TYPE_ANSWER, question.getADataTypen());
//        values.put(QuizDbHelper.ANSWER_STRUCTURE, question.getAStructure());
//        values.put(QuizDbHelper.CORRECT_ANSWER, question.getAnswer());
//        values.put(QuizDbHelper.REVIEW, question.getReview());
//
//        long insertId = database.insert(QuizDbHelper.TABLE_NAME, null, values);
//        Cursor cursor = database.query(QuizDbHelper.TABLE_NAME,
//                allColumns,
//                QuizDbHelper.ID + " = " + insertId,
//                null,null, null, null);
//        cursor.moveToFirst();//now the cursor has only one element but the index is -1, so we need to do cursor.moveToFirst()
//        Question mQuestion = cursorToQuestion(cursor);
//        Log.d("test", "Name = " + mQuestion.getmQuestion() +  " insert ID = " + mQuestion.getId());
//        cursor.close();
//        return insertId;
//    }
//
//    // Remove an entry by giving its index
//    public void removeEntry(long rowId) {
//        database.delete(QuizDbHelper.TABLE_NAME, QuizDbHelper.ID
//                + " = " + rowId, null);
//    }
//
//    // Query a specific entry by its index.
//    public Question fetchEntryByIndex(int rowId) {
//        Cursor cursor = database.query(QuizDbHelper.TABLE_NAME, allColumns,
//                QuizDbHelper.ID + " = " + rowId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        Question entry = new Question();
//        entry.setId(cursor.getInt(cursor.getColumnIndex(QuizDbHelper.ID)));
//        entry.setPerson(cursor.getString(cursor.getColumnIndex(QuizDbHelper.PERSON)));
//        entry.setQType(cursor.getString(cursor.getColumnIndex(QuizDbHelper.QUESTION_TYPE)));
//        entry.setQDataType(cursor.getString(cursor.getColumnIndex(QuizDbHelper.DATA_TYPE_QUESTION)));
//        entry.setQStructure(cursor.getString(cursor.getColumnIndex(QuizDbHelper.QUESTION_STRUCTURE)));
//        entry.setmQuestion(cursor.getString(cursor.getColumnIndex(QuizDbHelper.QUESTION)));
//        entry.setADataType(cursor.getString(cursor.getColumnIndex(QuizDbHelper.DATA_TYPE_ANSWER)));
//        entry.setAStructure(cursor.getString(cursor.getColumnIndex(QuizDbHelper.ANSWER_STRUCTURE)));
//        entry.setAnswer(cursor.getString(cursor.getColumnIndex(QuizDbHelper.CORRECT_ANSWER)));
//        entry.setReview(cursor.getInt(cursor.getColumnIndex(QuizDbHelper.REVIEW)) > 0);
//        cursor.close();
//        return entry;
//    }
//
//
//    // Query the entire table, return all rows
//    public List<Question> fetchEntries() {
//        List<Question> entries = new ArrayList<Question>();
//        Cursor cursor = database.query(QuizDbHelper.TABLE_NAME,
//                allColumns, null, null, null, null, null);
//        cursor.moveToFirst(); //Move the cursor to the first row.
//        while (!cursor.isAfterLast()) {//Returns whether the cursor is pointing to the position after the last row.
//            Question entry = cursorToQuestion(cursor);
//            entries.add(entry);
//            cursor.moveToNext();
//        }
//        // Make sure to close the cursor
//        cursor.close();
//        return entries;
//    }
//
//
//    private Question cursorToQuestion(Cursor cursor) {
//        Question question = new Question();
//        question.setId(cursor.getInt(cursor.getColumnIndex(QuizDbHelper.ID)));
//        question.setPerson(cursor.getString(cursor.getColumnIndex(QuizDbHelper.PERSON)));
//        question.setQType(cursor.getString(cursor.getColumnIndex(QuizDbHelper.QUESTION_TYPE)));
//        question.setQDataType(cursor.getString(cursor.getColumnIndex(QuizDbHelper.DATA_TYPE_QUESTION)));
//        question.setQStructure(cursor.getString(cursor.getColumnIndex(QuizDbHelper.QUESTION_STRUCTURE)));
//        question.setmQuestion(cursor.getString(cursor.getColumnIndex(QuizDbHelper.QUESTION)));
//        question.setADataType(cursor.getString(cursor.getColumnIndex(QuizDbHelper.DATA_TYPE_ANSWER)));
//        question.setAStructure(cursor.getString(cursor.getColumnIndex(QuizDbHelper.ANSWER_STRUCTURE)));
//        question.setAnswer(cursor.getString(cursor.getColumnIndex(QuizDbHelper.CORRECT_ANSWER)));
//        question.setReview(cursor.getInt(cursor.getColumnIndex(QuizDbHelper.REVIEW)) > 0);
//        return question;
//    }
//
//
//}
