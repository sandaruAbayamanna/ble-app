package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    TextView showTitle,showDetails;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("Notes Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showTitle = findViewById(R.id.showTitle);
        showDetails = findViewById(R.id.showDetails);

        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        Intent intent = getIntent();

        id = intent.getIntExtra("ID",0);
        NoteModel noteModel = db.getNotes(id);

        showTitle.setText(noteModel.getNoteTitle());
        showDetails.setText(noteModel.getNoteDetails());
        Toast.makeText(this, "id"+noteModel.getId(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;

        }

        if (item.getItemId() == R.id.delete){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Note");
            builder.setMessage("Are you sure you want to delete this note?");

            //Add positive button
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //delete note method
                    NoteDatabaseHelper db = new NoteDatabaseHelper(DetailActivity.this);
                    Intent intent = getIntent();
                    id = intent.getIntExtra("ID",0);
                    db.deleteNote(id);
                    Toast.makeText(DetailActivity.this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(DetailActivity.this,ListNotesActivity.class);
                    startActivity(intent1);
                }
            });

            //Add negative button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;



        }
        return super.onOptionsItemSelected(item);
    }
}