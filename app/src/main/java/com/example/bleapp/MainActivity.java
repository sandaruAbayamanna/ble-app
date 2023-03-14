package com.example.bleapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    public BluetoothAdapter bluetoothAdapter;
  //  public BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();


    TextView BluStatus, DeviceList;
    ImageView iconB;
    Button onBtn, offBtn, discoverableBtn;

    public static class BLEscanner{
        public BluetoothLeScanner bluetoothLeScanner;
        private boolean scanning;
        public Handler handler;

        // Stops scanning after 10 seconds.
        private static final long SCAN_PERIOD = 10000;

        LeDeviceListAdapter obj = new LeDeviceListAdapter();


        @SuppressLint("MissingPermission")
        public void scanLeDevice() {


            if (!scanning) {
                // Stops scanning after a predefined scan period.
                handler.postDelayed(new Runnable() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void run() {
                        scanning = false;
                        bluetoothLeScanner.stopScan(obj.leScanCallback);
                    }
                }, SCAN_PERIOD);

                scanning = true;
                bluetoothLeScanner.startScan(obj.leScanCallback);
            } else {
                scanning = false;
                bluetoothLeScanner.stopScan(obj.leScanCallback);
            }
        }
    }

    public static class LeDeviceListAdapter{

        public LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

        // Device scan callback.
        ScanCallback leScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                //scanned devices
                leDeviceListAdapter.addDevice(result.getDevice());
                leDeviceListAdapter.notifyDataSetChanged();
            }
        };

        private void notifyDataSetChanged() {
        }

        private void addDevice(BluetoothDevice device) {

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        BluStatus = findViewById(R.id.bluStatus);
        DeviceList = findViewById(R.id.listDv);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        discoverableBtn = findViewById(R.id.discoverableBtn);

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

            //@SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Turning On Bluetooth", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth Is Already ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //off button
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    bluetoothAdapter.disable();
                    Toast.makeText(MainActivity.this, "Turning Bluetooth OFF.....", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth Is Already OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //bluetooth scanning button
        discoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLEscanner bleScanner = new BLEscanner();
                bleScanner.scanLeDevice();

            }

        });


    }
}