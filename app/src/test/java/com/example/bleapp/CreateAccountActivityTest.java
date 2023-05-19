package com.example.bleapp;

import static org.junit.Assert.*;

import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {
    @Mock
    private FirebaseAuth mockFirebaseAuth;
    private CreateAccountActivity createAccountActivity;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        createAccountActivity = new CreateAccountActivity();
        //createAccountActivity.firebaseAuth = mockFirebaseAuth;
    }

    @Test
    public void testValidateData_ValidData_ReturnsTrue() {
        // Set up test data
        String email = "test@example.com";
        String password = "password";
        String confirmPassword = "password";

        // Call the method under test
        boolean isValidated = createAccountActivity.validateData(email, password, confirmPassword);

        // Verify the expected result
        assertTrue(isValidated);
    }

   /* @Test
    public void testValidateData_InvalidEmailFormat_ReturnsFalse() {
        // Set up test data
        String email = "invalidemail";
        String password = "password";
        String confirmPassword = "password";

        // Initialize the createAccountActivity object
        CreateAccountActivity createAccountActivity = new CreateAccountActivity();

        // Call the method under test
        boolean isValidated = createAccountActivity.validateData(email, password, confirmPassword);

        // Verify the expected result
        assertFalse(isValidated);
    }*/

    @After
    public void tearDown() throws Exception {
    }
}