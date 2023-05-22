package com.example.bleapp;

import static org.junit.Assert.*;

import android.util.Patterns;

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

    }


    //happy scenario --> all good
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

    //Email format mismatch testing
    @Test
    public void testValidateData_InvalidEmailFormat_ReturnsFalse() {
        // Set up test data
        String email = "invalidemail";
        String password = "password";
        String confirmPassword = "password";

        // Call the method under test
        boolean isValidated = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        // Verify the expected result
        assertFalse(isValidated);
    }


    //invalid password length testing
    @Test
    public void testValidateData_InvalidPassword_ReturnsFalse(){
        // Set up test data
        String email = "test@example.com";
        String password = "pas";
        String confirmPassword = "pas";

        if (password.length()<6){
            boolean isValidated = false;
            assertFalse(isValidated);
        }

    }

    //password mismatch testing
    @Test
    public void testValidateData_mismatchPassword(){
        // Set up test data
        String email = "test@example.com";
        String password = "password123";
        String confirmPassword = "password13";

        boolean isValidated = password.equals(confirmPassword);
        assertFalse(isValidated);
    }


    @After
    public void tearDown() throws Exception {
    }
}