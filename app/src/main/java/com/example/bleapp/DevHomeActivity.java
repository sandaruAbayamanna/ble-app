/*
package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DevHomeActivity extends AppCompatActivity {

    */
/*static String DEVICE_NAME = "DEVICE_NAME";
    static String DEVICE_ADDRESS = "DEVICE_ADDRESS";*//*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_home);

        Button addBtn = findViewById(R.id.btn_addNotes);
        Button viewBtn = findViewById(R.id.btn_viewNotes);
        TextView devName = findViewById(R.id.dev_name_text);
        TextView editAddr= findViewById(R.id.addr_edit);

       */
/* // Get the device address & Name from the Intent
        String deviceAddress = getIntent().getStringExtra("deviceAddress");
        String dev_name = getIntent().getStringExtra("dev_name");*//*


        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String deviceName = sharedPreferences.getString("device_name", "unknown Device");
        String deviceAddress = sharedPreferences.getString("device_address", "Address Not found!!");

        //devName.setText(dev_name);

        devName.setText(deviceName);
        editAddr.setText(deviceAddress);
        Log.i("DevHomeActivity","address is :"+deviceAddress+" "+deviceName);


        //can add notes directly
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DevHomeActivity.this,ListNotesActivity.class);
                startActivity(i);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DevHomeActivity.this,AddNoteActivity.class);
                startActivity(i);
            }
        });

    }
}*/
