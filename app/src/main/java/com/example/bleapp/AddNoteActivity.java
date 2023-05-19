package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    EditText title,details;
    Button addNoteBtn;
    String todayDate,currentTime,noteTitle,noteDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getSupportActionBar().setTitle("Add New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.addNote);
        details = findViewById(R.id.noteDetails);
        addNoteBtn = findViewById(R.id.addNoteBtn);

        //get current date & time
        Date currentDate = new Date();

        // Create a SimpleDateFormat instance to format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Format the date and time
        todayDate = dateFormat.format(currentDate);
        currentTime = timeFormat.format(currentDate);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 noteTitle = title.getText().toString().trim();
                 noteDetails = details.getText().toString().trim();

                //validating the note title has value
                if (noteTitle.isEmpty()){
                    title.setError("Please enter a note title");
                    title.requestFocus();
                    return;
                }

                //validating the note details containing value
                if (noteDetails.isEmpty()){
                    details.setError("Please enter the note details");
                    details.requestFocus();
                    return;
                }

                NoteModel noteModel = new NoteModel(noteTitle,noteDetails,todayDate,currentTime);
                NoteDatabaseHelper db = new NoteDatabaseHelper(AddNoteActivity.this);
                db.AddNote(noteModel);
                Log.i("note Model","added note is:"+noteModel.getNoteTitle());

                Intent intent = new Intent(AddNoteActivity.this,ListNotesActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Note Saved",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public String pad(int i){
        if (i<0)
            return "0" +i;
        return String.valueOf(i);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

       if(!isFinishing()){

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Discard Note");
           builder.setMessage("Are you sure you want to discard this note?");

           //positive result-->discard
           builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   AddNoteActivity.super.onBackPressed();
               }
           });

           //negative result-->cancel
           builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           });
           AlertDialog dialog = builder.create();
           builder.show();

       } else {
           super.onBackPressed();
       }
    }
}