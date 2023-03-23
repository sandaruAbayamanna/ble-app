package com.example.bleapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int REQUEST_ENABLE_BT = 1;
    //init scanLeDevice class
    private scanLeDevice mScanLeDevice;

    TextView BluStatus, DeviceList;
    ImageView iconB;
    Button onBtn, offBtn, scannerBtn;

    private HashMap<String, bleDevice> mBleDevicesHashMap;
    private ArrayList<bleDevice> mBleDevicesArrayList;
    private listAdapterBleDevices adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        BluStatus = findViewById(R.id.bluStatus);
        //no need remove once finished
        // DeviceList = findViewById(R.id.listDv);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scannerBtn = findViewById(R.id.scannerBtn);

        mScanLeDevice = new scanLeDevice(this, 7500, -75);

        //create new objects
        mBleDevicesHashMap = new HashMap<>();
        mBleDevicesArrayList = new ArrayList<>();

        adapter = new listAdapterBleDevices(this,R.layout.btle_device_list_item, mBleDevicesArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        //checking the bluetooth availability
        if (bluetoothAdapter == null) {
            BluStatus.setText("Bluetooth is not available");
        } else {
            BluStatus.setText("Bluetooth is available");
        }

        //setting up the bluetooth icon
        if (bluetoothAdapter.isEnabled()) {
            iconB.setImageResource(R.drawable.ic_action_on);
        } else {
            iconB.setImageResource(R.drawable.ic_action_off);
        }

        //on button
        onBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Turning On Bluetooth", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth Is Already ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //off button
        offBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled()) {

                    bluetoothAdapter.disable();
                    Toast.makeText(MainActivity.this, "Turning Bluetooth OFF.....", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth Is Already OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //bluetooth scanning button
        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter.isEnabled()){
                    ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
                }else {
                    Toast.makeText(MainActivity.this, "Please turn on bluetooth to scan devices", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }

    public void addDevice(BluetoothDevice device, int rssi) {
        String address = device.getAddress();
        if (!mBleDevicesHashMap.containsKey(address)) {
            bleDevice btleDevice = new bleDevice(device);
            btleDevice.setRssi(rssi);

            mBleDevicesHashMap.put(address, btleDevice);
            mBleDevicesArrayList.add(btleDevice);
        }
        else {
            Objects.requireNonNull(mBleDevicesHashMap.get(address)).setRssi(rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void stopScan() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.scannerBtn:
                Utils.toast(getApplicationContext(), "Scan Button Pressed");

                if (!mScanLeDevice.isScanning()) {
                    startScan();
                }
                else {
                    stopScan();
                }

                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScan(){
        //btn_Scan.setText("Scanning...");

        mBleDevicesArrayList.clear();
        mBleDevicesHashMap.clear();

        adapter.notifyDataSetChanged();

        mScanLeDevice.start();
    }
}