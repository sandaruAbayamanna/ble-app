package com.example.bleapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import java.util.ArrayList;

public class leDeviceListAdapter {
    // leDeviceListAdapter leDeviceListAdapter = new leDeviceListAdapter();
     private static ArrayList<BluetoothDevice> mLeDevices;

    public leDeviceListAdapter(ArrayList<BluetoothDevice> mLeDevices) {
        leDeviceListAdapter.mLeDevices = mLeDevices;
    }

    // Device scan callback
    static ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    leDeviceListAdapter.addDevice(result.getDevice());
                    leDeviceListAdapter.notifyDataSetChanged();
                }
            };


    private static void notifyDataSetChanged() {

    }

    private static void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)){
            mLeDevices.add(device);
        }
    }
}
