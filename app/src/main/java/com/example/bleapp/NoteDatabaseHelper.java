package com.example.bleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "MineNotesDB.db";
    public static final int DATABASE_VERSION = 4;

    public static String TABLE_NAME = "MineNotesTable";
    public static String COLUMN_ID = "NotesId";
    public static String COLUMN_TITLE= "NotesTitle";
    public static String COLUMN_DETAILS= "NotesDetails";
    public static String COLUMN_DATE= "NotesDate";
    public static String COLUMN_TIME= "NotesTime";

    public NoteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //create table schema
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_DETAILS + "TEXT," +
                        COLUMN_DATE +"TEXT," +
                        COLUMN_TIME + "TEXT" +")";

        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    //inserting values to the table
    public long AddNote(NoteModel noteModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabaseHelper.COLUMN_TITLE, noteModel.getNoteTitle());
        //No column error
        //contentValues.put(NoteDatabaseHelper.COLUMN_DETAILS, noteModel.getNoteDetails());
       // contentValues.put(NoteDatabaseHelper.COLUMN_DATE, noteModel. getNoteDate());
       // contentValues.put(NoteDatabaseHelper.COLUMN_TIME, noteModel.getNoteTime());

        //Hardcoded values
/*
        contentValues.put(NoteDatabaseHelper.COLUMN_TITLE, "My Title");
        contentValues.put(NoteDatabaseHelper.COLUMN_DETAILS, "My Description");*/


        long ID= db.insert(TABLE_NAME,null,contentValues);
        Log.i("Add note","sql data inserted ID Is: "+ID);

        if (ID > 0) {
            // Insert was successful
            Log.i("sql insert","Note saved successfully");
        } else {
            // Insert failed
            Log.i("sql insert","Error: Failed to save note");

        }
        return ID;

    }

    public List<NoteModel> getNote(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<NoteModel> allNote = new ArrayList<>();

        String queryStatement = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(queryStatement,null);

        if (cursor.moveToFirst()){
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setId(cursor.getInt(0));
                noteModel.setNoteTitle(cursor.getString(1));
                noteModel.setNoteDetails(cursor.getString(2));
                noteModel.setNoteDate(cursor.getString(3));
                noteModel.setNoteTime(cursor.getString(4));

                allNote.add(noteModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return allNote;
    }

    //display the note title with details
    public NoteModel getNotes(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] query = new String[]{COLUMN_ID,COLUMN_TITLE,COLUMN_DETAILS,COLUMN_DATE,COLUMN_TIME};
        Cursor cursor = db.query(TABLE_NAME, query,COLUMN_ID+"=?", new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();

        }

        assert cursor != null;
        return new NoteModel(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
    }

    void deleteNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID+"=?", new String[]{String.valueOf(id)});
        db.close();

    }

}
