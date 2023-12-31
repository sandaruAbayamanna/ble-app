package com.example.bleapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity{

    public static final int REQUEST_ENABLE_BT = 1;
    //init scanLeDevice class
    private scanLeDevice mScanLeDevice;

    TextView BluStatus, DeviceList;
    ImageView iconB;
    Button onBtn, offBtn, scannerBtn;

    private HashMap<String, bleDevice> mBleDevicesHashMap;
    private ArrayList<bleDevice> mBleDevicesArrayList;
    private listAdapterBleDevices adapter;
    private ListView listView;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private final static String TAG = MainActivity.class.getSimpleName();


    ///Requesting location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE)
        {
            //Do something based on grantResults
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Location Permission granted...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "coarse location permission granted");
            }
            else
            {
                Toast.makeText(this, "Location Permission denied...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "coarse location permission denied");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("activity","OnCreate");

        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        BluStatus = findViewById(R.id.bluStatus);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scannerBtn = findViewById(R.id.scannerBtn);

        //create new objects
        mBleDevicesHashMap = new HashMap<>();
        mBleDevicesArrayList = new ArrayList<>();

        mScanLeDevice = new scanLeDevice(this, 7500, -75);

        //checking the bluetooth availability
        if (bluetoothAdapter == null) {
            BluStatus.setText(R.string.bl_unavailable);
        } else {
            BluStatus.setText(R.string.bl_available);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("activity","OnStart");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("activity","OnResume");
        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!mScanLeDevice.isScanning()) {
                        startScan();
                        Log.i("MainActivity.java","scan method start through btn pressed!!!!!!!");
                    }
                    else {
                        stopScan();
                    }
                }

                Log.i("btn","scanner btn called");
                /*listView = findViewById(R.id.list_view);*/
                List<bleDevice> devList= new ArrayList<>();
                //adding lists
                //how should add????
                // devList.add(new bleDevice(device));
                for (bleDevice device : mBleDevicesArrayList){
                    devList.add(device);
                    Log.i("dev List","adding to the list view today##");
                }

                Log.i("onResume","outside the device add method ");
                listAdapterBleDevices adapter = new listAdapterBleDevices(getApplicationContext(), (ArrayList<bleDevice>) devList);
                listView.setAdapter(adapter);
                Log.i("adapter",""+adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //get position of the item from the list
                        bleDevice item = (bleDevice) parent.getItemAtPosition(position);
                        //Toast.makeText(MainActivity.this, item.getAddress(), Toast.LENGTH_SHORT).show();

                        // Create an Intent to start the authentication activity
                        Intent intent = new Intent(MainActivity.this,AuthActivity.class);

                        //using shared preferences to store device name& address
                        SharedPreferences sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("device_name", item.getName());
                        editor.putString("device_address", item.getAddress());
                        editor.apply();
                       /* //pass the device name & Address to the AuthActivity
                        intent.putExtra(AuthActivity.DEVICE_NAME,item.getName());
                        intent.putExtra(AuthActivity.DEVICE_ADDRESS,item.getAddress());

                        //pass the device name & Address to the DevHomeActivity
                        intent.putExtra(DevHomeActivity.DEVICE_NAME,item.getName());
                        intent.putExtra(DevHomeActivity.DEVICE_ADDRESS,item.getAddress());*/

                        // Start the authentication activity
                        startActivity(intent);
                    }
                });
            }
        });



    }

    public void addDevice(BluetoothDevice device, int rssi) {
        if (!mBleDevicesArrayList.contains(device)){
            bleDevice btleDevice = new bleDevice(device);
            mBleDevicesArrayList.add(btleDevice);
        }
        /*//Log.i("in the adapter class","getting results to the add method" +device);//ok
        String address = device.getAddress();

        //create new objects
        mBleDevicesHashMap = new HashMap<>();
        mBleDevicesArrayList = new ArrayList<>();

        if (!mBleDevicesHashMap.containsKey(address)) {
            bleDevice btleDevice = new bleDevice(device);
            btleDevice.setRssi(rssi);

            mBleDevicesHashMap.put(address, btleDevice);
            mBleDevicesArrayList.add(btleDevice);
            //Log.i("adding to the list","addddddd.."+btleDevice);//ok
        }
        else {
            Objects.requireNonNull(mBleDevicesHashMap.get(address)).setRssi(rssi);
        }

        //adapter.notifyDataSetChanged();*/
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScan(){

        mBleDevicesArrayList.clear();
        mBleDevicesHashMap.clear();
       // adapter.notifyDataSetChanged();

        mScanLeDevice.start();
        Log.i("mainActivity.java","start scanning method called");



    }
    public void stopScan() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}