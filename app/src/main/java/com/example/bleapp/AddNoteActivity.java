package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    EditText title,details;
    Button addNoteBtn;
    String todayDate,currentTime;
    Calendar calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getSupportActionBar().setTitle("Add New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.addNote);
        details = findViewById(R.id.noteDetails);
        addNoteBtn = findViewById(R.id.addNoteBtn);

        calender = Calendar.getInstance();
        todayDate= calender.get(Calendar.YEAR)+"/"+calender.get(Calendar.MONTH)+"/"+calender.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(calender.get(Calendar.HOUR))+":"+pad(calender.get(Calendar.MINUTE));

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteModel noteModel = new NoteModel(title.getText().toString(),details.getText().toString(),todayDate,currentTime);
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
}