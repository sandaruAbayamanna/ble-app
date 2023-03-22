package com.example.bleapp;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.bleapp.leDeviceListAdapter.leScanCallback;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")
public class scanLeDevice {

    private BluetoothAdapter bluetoothAdapter;
    public BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning;
    public Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    if (!scanning) {
        // Stops scanning after a predefined scan period.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
            }
        }, SCAN_PERIOD);

        scanning = true;
        bluetoothLeScanner.startScan(leScanCallback);
    } else {
        scanning = false;
        bluetoothLeScanner.stopScan(leScanCallback);
    }
}
