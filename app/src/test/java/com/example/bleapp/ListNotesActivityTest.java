package com.example.bleapp;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowLooper;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ListNotesActivityTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSignOutUser(){
        // Create an instance of the activity under test
        ListNotesActivity activity = Robolectric.buildActivity(ListNotesActivity.class).create().get();

        activity.showMenu();

        // Retrieve the pop-up menu reference from the activity
        PopupMenu popupMenu = activity.popupMenu;

        // Simulate click event on the logout menu item
        MenuItem logoutItem = popupMenu.getMenu().findItem(0); // Assuming there is only one item in the menu
        activity.runOnUiThread(() -> {
            popupMenu.getMenu().performIdentifierAction(logoutItem.getItemId(), 0);
        });

        // Assert the expected behavior after logout
        // For example, you can check if the activity transitions to the login screen
        Intent expectedIntent = new Intent(activity, MainActivity.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        activity.closeContextMenu();

    }

   /* @Test
    public void testFab(){

        // Create an instance of the activity under test
        ListNotesActivity activity2 = Robolectric.buildActivity(ListNotesActivity.class).create().get();

        FloatingActionButton fab = activity2.findViewById(R.id.addNoteBtn);
        fab.performClick();

        Intent expectedIntent=new Intent(activity2,AddNoteActivity.class);
        ShadowActivity shadowActivity = shadowOf(activity2);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(),actualIntent.getComponent());
    }*/


    @Test
    public void testAddNoteToDb(){
        AddNoteActivity activity = Robolectric.buildActivity(AddNoteActivity.class).create().get();

        EditText titleEditText = activity.findViewById(R.id.addNote);
        EditText descEditText = activity.findViewById(R.id.noteDetails);

        titleEditText.setText("sample title");
        descEditText.setText("sample content");

        // Simulate click event on the "Add" button
        Button addButton = activity.findViewById(R.id.addNoteBtn);
        addButton.performClick();

        // Allow pending tasks in the main looper to execute
        Robolectric.flushForegroundThreadScheduler();


        // Verify that the notes details are correctly added to the SQLite database
        NoteDatabaseHelper databaseHelper = new NoteDatabaseHelper(activity);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM TABLE_NAME WHERE COLUMN_TITLE = 'Sample Title'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        database.close();
        databaseHelper.close();

        assertEquals(1, rowCount);


    }
    @After
    public void tearDown() throws Exception {
    }
}