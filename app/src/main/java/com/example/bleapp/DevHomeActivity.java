package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DevHomeActivity extends AppCompatActivity {

    static String DEVICE_NAME = "DEVICE_NAME";
    static String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private BluetoothDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_home);

        Button addBtn = findViewById(R.id.btn_addNotes);
        Button viewBtn = findViewById(R.id.btn_viewNotes);
        TextView devName = findViewById(R.id.dev_name_text);
        TextView editAddr= findViewById(R.id.addr_edit);

        // Get the device address & Name from the Intent
        String deviceAddress = getIntent().getStringExtra(DEVICE_ADDRESS);
        String dev_name = getIntent().getStringExtra(DEVICE_NAME);

        /*// Get the BluetoothDevice for the selected address
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        mDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);*/

        devName.setText(dev_name);
        editAddr.setText(deviceAddress);

    }
}