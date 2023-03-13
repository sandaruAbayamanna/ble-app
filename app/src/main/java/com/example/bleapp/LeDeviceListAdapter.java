package com.example.bleapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;



//BLE scan results devices listdown
public class LeDeviceListAdapter{

    private final LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

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
