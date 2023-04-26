package com.example.bleapp;

import static com.example.bleapp.NoteDatabaseHelper.COLUMN_DEVICENAME;
import static com.example.bleapp.NoteDatabaseHelper.COLUMN_PASSWORD_HASH;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AuthActivity extends AppCompatActivity {

    /*//get the passed device name & address
    static String deviceName = "DEVICE_NAME";
    static String DEVICE_ADDRESS = "DEVICE_ADDRESS";*/
    private BluetoothDevice mDevice;
    private EditText mPasswordEdit;
    //NoteDatabaseHelper noteDatabaseHelper;

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

                SharedPreferences sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("password", password);
                editor.apply();

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void authenticate(String password) {
        Log.i("auth", "authentication started");

        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String deviceName = sharedPreferences.getString("device_name", "unknown Device");
        String pass = sharedPreferences.getString("password",null);

        db.addUser(deviceName,password);
        db.authenticateUser(deviceName,password);
        db.close();

        String storedSalt = sharedPreferences.getString("salt", null);
        String storedHashedPassword = sharedPreferences.getString("hashedPassword", null);
        //Here check whether the hash pass equals to the entered one and proceed

        NoteDatabaseHelper noteDatabaseHelper = new NoteDatabaseHelper(this);
        SQLiteDatabase dbe = noteDatabaseHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_DEVICENAME,
                COLUMN_PASSWORD_HASH
        };

        String selection = COLUMN_DEVICENAME + " = ?";
        String[] selectionArgs = { deviceName };


        Cursor cursor = dbe.query(
                NoteDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        /*SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("salt", salt);
        editor.putString("hashedPassword", hashedPassword);
        editor.apply();*/

        if (cursor.moveToFirst()) {
            String storedPasswordHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_HASH));

            if (BCrypt.checkpw(password, storedPasswordHash)) {
                // Password matches, allow user to proceed to main activity
                Intent intent = new Intent(AuthActivity.this,DevHomeActivity.class);
                startActivity(intent);
            } else {
                // Password does not match, display error message
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // User not found, display error message
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    /*private Cursor query(String[] projection, String selection, String[] selectionArgs) {

        return COLUMN_DEVICENAME;
    }*/


      /*  if (storedSalt != null && storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)) {
            // Password is correct
            Intent intent = new Intent(AuthActivity.this,DevHomeActivity.class);
            startActivity(intent);
        } else {
            // Password is incorrect
            Toast.makeText(this, "Password is invalid please try again !!!!!", Toast.LENGTH_SHORT).show();
        }*/

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

    //}






    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}