package com.example.bleapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;



public class LeDeviceListAdapter {

    private final LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

    // Device scan callback.
     ScanCallback leScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                         super.onScanResult(callbackType, result);
                        leDeviceListAdapter.addDevice(result.getDevice());
                        leDeviceListAdapter.notifyDataSetChanged();
                }
            };

    private void notifyDataSetChanged() {
    }

    private void addDevice(BluetoothDevice device) {
        
    }


}
