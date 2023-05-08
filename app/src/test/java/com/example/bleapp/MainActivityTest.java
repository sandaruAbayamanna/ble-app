package com.example.bleapp;

import static org.junit.Assert.*;

import android.view.View;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity =null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void TestLaunch(){
        View view = mActivity.findViewById(R.layout.activity_splash);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity =null;
    }
}