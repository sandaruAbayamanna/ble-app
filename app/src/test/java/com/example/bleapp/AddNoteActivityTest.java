package com.example.bleapp;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class AddNoteActivityTest {

    @Test
    public void testAddNoteInputValidation() {
        AddNoteActivity activity = Robolectric.buildActivity(AddNoteActivity.class).create().get();

        // Case 1: Empty title, non-empty note details
        EditText titleEditText = activity.findViewById(R.id.addNote);
        EditText descEditText = activity.findViewById(R.id.noteDetails);
        titleEditText.setText(""); // Set an empty title
        descEditText.setText("Sample note details");

        // Simulate click event on the "Add" button
        Button addButton = activity.findViewById(R.id.addNoteBtn);
        addButton.performClick();
        shadowOf(Looper.getMainLooper()).idle();

        // Verify that an error message is displayed for the empty title
        String error = titleEditText.getError().toString();
        assertEquals("Please enter a note title", error);

        // Case 2: Non-empty title, empty note details
        titleEditText.setText("Sample title");
        descEditText.setText(""); // Set empty note details

        addButton.performClick();

         // Verify that an error message is displayed for the empty title
        String errorDesc = descEditText.getError().toString();
        assertEquals("Please enter the note details", errorDesc);

        // Case 3: Non-empty title, non-empty note details
        titleEditText.setText("Sample title");
        descEditText.setText("Sample note details");

        String valhalla= titleEditText.getText().toString();
        String combio = descEditText.getText().toString();

        addButton.performClick();

        // Verify that no error message is displayed

        String titleMsg = titleEditText.getText().toString().trim();
        String descMsg  =descEditText.getText().toString().trim();
        assertEquals(valhalla, titleMsg);
        assertEquals(combio, descMsg);
    }

}