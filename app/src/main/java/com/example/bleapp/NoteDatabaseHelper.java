package com.example.bleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "NotesTable";
    public static final String COLUMN_ID = "NotesId";
    public static final String COLUMN_TITLE= "NotesTitle";
    public static final String COLUMN_DETAILS= "NotesDetails";
    public static final String COLUMN_DATE= "NotesDate";
    public static final String COLUMN_TIME= "NotesTime";

    public NoteDatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    public long AddNote(NoteModel noteModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, noteModel.getNoteTitle());
        contentValues.put(COLUMN_DETAILS, noteModel.getNoteDetails());
        contentValues.put(COLUMN_DATE, noteModel. getNoteDate());
        contentValues.put(COLUMN_TIME, noteModel.getNoteTime());

        return db.insert(TABLE_NAME, null,contentValues);

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

        return allNote;
    }

}
