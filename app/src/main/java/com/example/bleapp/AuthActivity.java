package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class AuthActivity extends AppCompatActivity {

    /*//get the passed device name & address
    static String DEVICE_NAME = "DEVICE_NAME";
    static String DEVICE_ADDRESS = "DEVICE_ADDRESS";*/
    private BluetoothDevice mDevice;
    private EditText mPasswordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Get references to the EditText and Button
        mPasswordEdit = findViewById(R.id.password_edit);
        Button mAuthButton = findViewById(R.id.auth_button);

       /* // Get the device address & Name from the Intent
        String deviceAddress = getIntent().getStringExtra(DEVICE_ADDRESS);
        String deviceName = getIntent().getStringExtra(DEVICE_NAME);*/

      /*  // Get the BluetoothDevice for the selected address
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        mDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);*/

        // Set the device name in the TextView
        TextView deviceNameText = findViewById(R.id.device_name_text);

        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String deviceName = sharedPreferences.getString("device_name", "unknown Device");

        deviceNameText.setText(deviceName);

        //deviceNameText.setText(deviceName);
        Log.i("device name","Dev name is : "+deviceName);

        // Set a click listener on the Button
        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("authButton", "login btn clicked");
                // Get the password from the EditText
                String password = mPasswordEdit.getText().toString();

                Utils.toast(getApplicationContext(), "Login Button Pressed");
                Log.i("authButton", "login btn clicked");


                // Start the authentication process
                authenticate(password);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void authenticate(String password) {
        Log.i("auth", "authentication started");

        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        db.addUser(password);

        //Here check whether the hash pass equals to the entered one and proceed
        /*if (password.equals("admin")){

           *//* // Get the device address & Name from the Intent
            String deviceAddress = getIntent().getStringExtra(DEVICE_ADDRESS);
            String deviceName = getIntent().getStringExtra(DEVICE_NAME);*//*
            // Create an Intent to start the authentication activity
            Intent intent = new Intent(AuthActivity.this,DevHomeActivity.class);
          *//*  //pass the device name & Address to the DevHomeActivity
            intent.putExtra("dev_name",deviceName);
            intent.putExtra("deviceAddress",deviceAddress);*//*
            startActivity(intent);
        }else{
            Toast.makeText(this, "Password is invalid please try again !!!!!", Toast.LENGTH_SHORT).show();
        }*/

    }






    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}