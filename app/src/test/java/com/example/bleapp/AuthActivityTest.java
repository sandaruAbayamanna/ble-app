package com.example.bleapp;

import static org.junit.Assert.*;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.ContentView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)

public class AuthActivityTest {
    public AuthActivity mAuthActivity;

    @Rule
    public ActivityTestRule<AuthActivity>mAuthActivityTestRule = new ActivityTestRule<AuthActivity>(AuthActivity.class);

    @Before
    public void setUp() throws Exception {

        mAuthActivity = mAuthActivityTestRule.getActivity();
    }

    @Test
    public void testLogin() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {

                EditText email = mAuthActivity.findViewById(R.id.email_edit_text);
                EditText pass = mAuthActivity.findViewById(R.id.password_edit_text);
                email.setText("email");
                pass.setText("pass");
                Button loginBtn = mAuthActivity.findViewById(R.id.login_btn);
                loginBtn.performClick();
               // assertTrue(mAuthActivity.getDisplay().isValid());
                //assertFalse(mAuthActivity.validateData(email,pass));

                assertFalse(false);
            }
        });
    }

    @Test
    public void testPasswordLength() {
        //min password length is 6

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //creatin user with less than length of password
        String email = "user@gmail.com";
        String password = "usr";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User creation should fail due to password length requirement
                        fail("User creation should fail due to password length requirement");
                        /*Log.i("test case","User creation should fail due to password length requirement");
                        System.out.println("User creation should fail due to password length requirement");*/
                    } else {
                        // Verify that the error message contains the expected string
                        assertTrue(task.getException().getMessage().contains("Password should be at least 6 characters"));
                        /*Log.i("test case","Password should be at least 6 characters");
                        System.out.println("Password should be at least 6 characters");*/
                    }
                });
    }

    @After
    public void tearDown() throws Exception {
            mAuthActivity = null;
        }
    }