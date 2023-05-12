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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ListNotesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton addBtn,signOutBtn;
    Adapter adapter;
    List<NoteModel> noteModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.addRecyclerView);
        addBtn = findViewById(R.id.add_note_btn);
        signOutBtn = findViewById(R.id.menu_btn);

        NoteDatabaseHelper noteDatabaseHelper = new NoteDatabaseHelper(this);
        noteModelList = noteDatabaseHelper.getNote();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(this,noteModelList);
        recyclerView.setAdapter(adapter);


        addBtn.setOnClickListener(v -> addNewActivity());
        signOutBtn.setOnClickListener(v -> showMenu());

    }
    public PopupMenu popupMenu;
    public void showMenu() {
        popupMenu = new PopupMenu(ListNotesActivity.this,signOutBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ListNotesActivity.this,MainActivity.class));
                    finish();
                    return true;

                }
                return false;
            }
        });
    }

    private void addNewActivity() {
        Intent floatIntent =new Intent(ListNotesActivity.this, AddNoteActivity.class);
        startActivity(floatIntent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addNote);
        Intent i =new Intent(ListNotesActivity.this, AddNoteActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Create an intent to navigate back to the previous activity
        Intent intent = new Intent(ListNotesActivity.this, MainActivity.class);
        startActivity(intent);

    }
}