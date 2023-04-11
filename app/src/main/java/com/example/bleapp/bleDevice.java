package com.example.bleapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

public class bleDevice {
    private final BluetoothDevice bluetoothDevice;
    private int rssi;

    public bleDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
    public String getAddress(){
        return bluetoothDevice.getAddress();
    }
    @SuppressLint("MissingPermission")
    public String getName(){
        return bluetoothDevice.getName();
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
