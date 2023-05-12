package com.example.bleapp;

import static android.os.Trace.isEnabled;
import static org.junit.Assert.*;

import android.bluetooth.BluetoothAdapter;
import android.view.View;
import android.widget.Button;

import androidx.annotation.ContentView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ThreadPoolExecutor;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<splashActivity> mActivityTestRule = new ActivityTestRule<splashActivity>(splashActivity.class);

    @Rule
    public ActivityTestRule<MainActivity>mMainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private splashActivity mSplashActivity;

    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
        mSplashActivity =mActivityTestRule.getActivity();

    }

    @Test
    public void TestLaunch(){
        View view = mSplashActivity.findViewById(R.layout.activity_splash);
        assertNotNull(view);
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

    @Test
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
                scenario.close();

            });
        });
    }
    @After
    public void tearDown() throws Exception {
        mSplashActivity =null;
    }
}