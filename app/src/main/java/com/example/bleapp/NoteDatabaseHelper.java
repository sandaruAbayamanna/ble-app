package com.example.bleapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "MineNotesDB.db";
    public static final int DATABASE_VERSION = 4;

    public static String TABLE_NAME = "MineNotesTable";
    public static String COLUMN_ID = "NotesId";
    public static String COLUMN_TITLE= "NotesTitle";
    public static String COLUMN_DETAILS= "NotesDetails";
    public static String COLUMN_DATE= "NotesDate";
    public static String COLUMN_TIME= "NotesTime";

    private static final String USER_TABLE_NAME = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD_HASH = "password_hash";

    private static final String query_user =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                    COLUMN_PASSWORD_HASH + " TEXT NOT NULL)";


    public NoteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //create table schema
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create note table
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_DETAILS + " TEXT," +
                        COLUMN_DATE +" TEXT," +
                        COLUMN_TIME + " TEXT" +")";

        db.execSQL(query);

        //create user table
        db.execSQL(query_user);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
        db.close();
    }

    //inserting values to the table
    public long AddNote(NoteModel noteModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabaseHelper.COLUMN_TITLE, noteModel.getNoteTitle());

        contentValues.put(NoteDatabaseHelper.COLUMN_DETAILS, noteModel.getNoteDetails());
        contentValues.put(NoteDatabaseHelper.COLUMN_DATE, noteModel. getNoteDate());
        contentValues.put(NoteDatabaseHelper.COLUMN_TIME, noteModel.getNoteTime());

        //Hardcoded values
/*
        contentValues.put(NoteDatabaseHelper.COLUMN_TITLE, "My Title");
        contentValues.put(NoteDatabaseHelper.COLUMN_DETAILS, "My Description");*/


        long ID= db.insert(TABLE_NAME,null,contentValues);

        //close the database connection
        db.close();
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
        db.close();
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

    public void addUser(String password){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD_HASH, hashPassword(password));
        db.insert(TABLE_NAME, null, values);
    }

    private String hashPassword(String password) {
        // Use a secure hashing algorithm such as bcrypt to hash the password
        // For example:
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);

    }

    public boolean authenticateUser(String username, String password) {
         SQLiteDatabase db = this.getReadableDatabase();
            String[] projection = { COLUMN_PASSWORD_HASH };
            String selection = COLUMN_USERNAME + " = ?";
            String[] selectionArgs = { username };
            Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                String passwordHash = cursor.getString(0);
                return BCrypt.checkpw(password, passwordHash);
            }

        return false;
    }

}
