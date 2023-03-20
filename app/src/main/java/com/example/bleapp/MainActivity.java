package com.example.bleapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    //defining list to data bind to the recyclerview
    private List<LeDeviceListAdapter> ledeviceLists = new ArrayList<>();
    private leDeviceAdapter leDevadpt;
    //  public BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    @SuppressLint("NewApi")
    BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
    BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
    private boolean scanning;
    Handler handler = new Handler();
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    LeDeviceListAdapter mLeDeviceListAdapter;


    TextView BluStatus, DeviceList;
    ImageView iconB;
    Button onBtn, offBtn, scannerBtn;
    RecyclerView recyclerView;


    @SuppressLint("MissingPermission")
    public void scanLeDevice(final boolean enable) {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            scanning = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @SuppressLint({"NewApi", "MissingPermission"})
    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        // setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    public class LeDeviceListAdapter {
        private ArrayList<BluetoothDevice> mleDevices;
        private LayoutInflater mInflator;

        /*// Device scan callback.
        ScanCallback leScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                //scanned devices
                ml.addDevice(result.getDevice());
                obj.notifyDataSetChanged();
            }
        };

        private void notifyDataSetChanged() {
        }

        private void addDevice(BluetoothDevice device) {

        }*/
        public LeDeviceListAdapter() {
            super();
            mleDevices = new ArrayList<BluetoothDevice>();
            mInflator = MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mleDevices.contains(device)) {
                mleDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mleDevices.get(position);
        }

        public void clear() {
            mleDevices.clear();
        }

        public int getCount() {
            return mleDevices.size();
        }

        public Object getItem(int i) {
            return mleDevices.get(i);
        }

        public long getItemId(int i) {
            return i;
        }

        public void notifyDataSetChanged() {

        }

        public void setOnClickListener(@Nullable View.OnClickListener l) {
            scannerBtn.setOnClickListener(l);
            int i = 0;
            BluetoothDevice device = mLeDeviceListAdapter.mleDevices.get(i);
            @SuppressLint("MissingPermission")
            final String deviceName = device.getName();

            if (deviceName != null && deviceName.length() > 0)
                DeviceList.append(deviceName);
            else
                DeviceList.append("unknown_device");
            DeviceList.append(device.getAddress());
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int i, byte[] bytes) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize bluetooth adapter
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        BluStatus = findViewById(R.id.bluStatus);
        DeviceList = findViewById(R.id.listDv);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scannerBtn = findViewById(R.id.scannerBtn);
        recyclerView = findViewById(R.id.recyclerView);

        //checking if the bluetooth supported on the device
        if (bluetoothAdapter == null) {
            BluStatus.setText("Bluetooth is not available");
        } else {
            BluStatus.setText("Bluetooth is available");
        }

        //setting up the bluetooth icon
        assert bluetoothAdapter != null;
        if (bluetoothAdapter.isEnabled()) {
            iconB.setImageResource(R.drawable.ic_action_on);
        } else {
            iconB.setImageResource(R.drawable.ic_action_off);
        }

        //on button
        onBtn.setOnClickListener(new View.OnClickListener() {

            //@SuppressLint("MissingPermission")
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


      /*  //bluetooth scanning button
        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                BluetoothDevice device = mLeDeviceListAdapter.mleDevices.get(i);
                if(bluetoothAdapter.isEnabled()){
                    DeviceList.setText("Available Devices");
                    @SuppressLint("MissingPermission")
                    //Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

                    for (BluetoothDevice device:getDevice()){
                        DeviceList.append("\nDevice" +device.getName()+"," +device);
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please turn on bluetooth to scan devices", Toast.LENGTH_SHORT).show();
                }

                //append data to the recycler view

                *//*if(bluetoothAdapter.isEnabled()){

                    @SuppressLint("MissingPermission")
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for (BluetoothDevice device:devices){
                        recyclerView.setAdapter("\nDevice" +device.getName()+"," +device);
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please turn on bluetooth to scan devices", Toast.LENGTH_SHORT).show();
                }*//*

                //ledeviceLists.add(bleScanner.obj);

                leDevadpt = new leDeviceAdapter(ledeviceLists);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(leDevadpt);

            }

        });*/


    }
}