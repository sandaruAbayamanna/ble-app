package com.example.bleapp;

import static org.junit.Assert.*;

import android.content.Context;

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
    private FirebaseAuth mAuth;


    @Rule
    public ActivityTestRule<AuthActivity>mAuthActivityTestRule = new ActivityTestRule<AuthActivity>(AuthActivity.class);

    @Before
    public void setUp() throws Exception {

        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testLoginWithCorrectCredentials(){
        mAuth.signInWithEmailAndPassword("sandaruabayamanna@gmail.com", "admin123")
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    assertNotNull(user);
                })
                .addOnFailureListener(exception -> {
                    fail("Login failed: " + exception.getMessage());
                });
    }

    @After
    public void tearDown() throws Exception {
    }
}