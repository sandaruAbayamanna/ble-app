package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ListNotesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    List<NoteModel> noteModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        recyclerView = findViewById(R.id.addRecyclerView);

        NoteDatabaseHelper noteDatabaseHelper = new NoteDatabaseHelper(this);
        noteModelList = noteDatabaseHelper.getNote();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);

        List<NoteModel> noteModelList = new ArrayList<>();
        noteModelList.add(new NoteModel("test","test","test","test"));
        noteModelList.add(new NoteModel("Synergen","dailyProgress","2023/04/19","2.53 P.M"));
        noteModelList.add(new NoteModel("test123....","test..","test","test"));
        noteModelList.add(new NoteModel("Synergen","dailyProgress","2023/04/19","2.53 P.M"));
        adapter = new Adapter(this,noteModelList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addNote);
        Intent i =new Intent(ListNotesActivity.this, AddNoteActivity.class);
        startActivity(i);

        return super.onOptionsItemSelected(item);
    }
}