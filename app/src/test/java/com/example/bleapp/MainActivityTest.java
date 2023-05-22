package com.example.bleapp;

import static android.os.Trace.isEnabled;
import static org.junit.Assert.*;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.ContentView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<splashActivity> mActivityTestRule = new ActivityTestRule<splashActivity>(splashActivity.class);

    @Rule
    public ActivityTestRule<MainActivity>mMainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private splashActivity mSplashActivity;
    private MainActivity mainActivity;
    private Button scanButton;
    private ListView listView;
    private ProgressDialog progressDialog;

    @Before
    public void setUp() throws Exception {

        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();



    }

    @Test
    public void testBluetoothOnBtnClick(){
        ActivityScenario<MainActivity>scenario =ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            Button blueOnBtn = activity.findViewById(R.id.onBtn);
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean isEnabled = bluetoothAdapter.isEnabled();
            blueOnBtn.performClick();
            boolean isEnabledAfterClick = bluetoothAdapter.isEnabled();

            //before clicking the blue on btn status !== after clicking the blu status ==>{means bluetooth on}
            Assert.assertNotEquals(isEnabled(),isEnabledAfterClick);

        });

    }

    @Test
    public void testBluetoothOffBtnClick(){
        ActivityScenario<MainActivity>scenario =ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Button bluOffBtn = activity.findViewById(R.id.offBtn);
            bluOffBtn.performClick();
            assertFalse(bluetoothAdapter.isEnabled());
        });

    }

    /*@Test
    public void testScanningBleBtn(){
        ActivityScenario<MainActivity>scenario =ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            Button bleScanBtn = activity.findViewById(R.id.scannerBtn);
            bleScanBtn.performClick();

            try {
                Thread.sleep(5000);
            }catch (InterruptedException err){
                err.printStackTrace();
            }
            scenario.onActivity(activity1 -> {
                View listview = activity.findViewById(R.id.list_view);

                assertNotNull(listview);


            });

        });
        scenario.close();
    }*/

    @Test
    public void testScanButton() throws InterruptedException {

        /*// Inflate the separate layout that contains the progress bar
        Context context = ApplicationProvider.getApplicationContext();
        View progressBarLayout = LayoutInflater.from(context).inflate(R.layout.progressbar_dialog, null);

        // Access the progress bar view
        ProgressBar progressBar = progressBarLayout.findViewById(R.id.progress_bar);
        // Create a CountDownLatch with a count of 1
        CountDownLatch latch = new CountDownLatch(1);*/

        // Launch the MainActivity
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        /*// Register an activity callback to intercept the opening of the progress bar dialog
        scenario.onActivity(activity -> {
            // Perform click event on the scan button
            Button scanButton = activity.findViewById(R.id.scannerBtn);
            scanButton.performClick();

            // Simulate a delay of 2000ms for the progress bar dialog to open
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                // Perform assertions or actions on the progress bar dialog
                assertNotNull(progressBar);
               // assertTrue(progressDialog.isShowing());  // Replace with your own logic to check if the progress bar dialog is showing

                // Notify the CountDownLatch to continue the test
                latch.countDown();
            }, 7);
        });

        // Wait for the progress bar dialog to open and close
        latch.await();*/

        // Continue with the remaining test steps
        scenario.onActivity(activity -> {
            // Perform assertions or actions on the ListView after the progress bar dialog is closed
            ListView listView = activity.findViewById(R.id.list_view);
            assertNotNull(listView);
        });

        // Close the activity scenario
        scenario.close();
    }
    @After
    public void tearDown() throws Exception {
        mSplashActivity =null;
    }
}