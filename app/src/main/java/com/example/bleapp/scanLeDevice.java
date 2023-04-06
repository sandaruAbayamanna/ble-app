package com.example.bleapp;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;



@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")
public class scanLeDevice extends ListActivity {

    private BluetoothAdapter mbluetoothAdapter;
    private MainActivity ma;
    private boolean mScanning;
    private Handler mHandler;
    private long SCAN_PERIOD = 10000;
    private int signalStrength;


    //constructor
    public scanLeDevice(MainActivity mainActivity, long SCAN_PERIOD, int signalStrength) {
        ma = mainActivity;
        mHandler = new Handler();

        this.SCAN_PERIOD = SCAN_PERIOD;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager = (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);

        mbluetoothAdapter = bluetoothManager.getAdapter();

    }

    public boolean isScanning() {
        return mScanning;
    }

    public void start() {

        if (!Utils.checkBluetooth(mbluetoothAdapter)) {
            Utils.requestUserBluetooth(ma);
            //refer mainActivity stop method
            ma.stopScan();
        } else {

            scannerDevice(true);

        }
    }

    public void stop() {
        scannerDevice(false);
    }

    private void scannerDevice(final boolean enable) {
        if (enable && !mScanning) {
            // Stops scanning after a predefined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mbluetoothAdapter.stopLeScan(mLeScanCallback);
                    ma.stopScan();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mbluetoothAdapter.startLeScan(mLeScanCallback);
           // Log.i("start scan","start Le scanning");

            /*//additional code to log and check whether call backs are returning//ok
            if (mLeScanCallback == null){
                Log.i("scanLeDevice.java","call back is null");//ok
            }else{
                Log.i("scanLeDevice.java","call back is not null"+ String.valueOf(mLeScanCallback));//ok
                System.out.println(mLeScanCallback);

            }*/

          //  Log.i("scanLeDevice.java","scanning begins .....");//ok

        } else {
            mScanning = false;
            mbluetoothAdapter.stopLeScan(mLeScanCallback);
           // Log.i("scanLeDevice.java","scanning stopped....");
        }
    }
    // Device scan callback.
    //
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    final int new_rssi = rssi;
                    if (rssi > signalStrength) {
                       // Log.i("scanLeDevice.java","rssi checking"+rssi); //working
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //ma.addDevice(device, new_rssi);
                                listAdapterBleDevices.addDevice(device, new_rssi);//adding to the list adapter
                                //Log.i("shakaboom","devices that are aired :"+device+" "+"RSSI:"+new_rssi);//showing MAC Addresses
                                //Log.i("scanLeDevice.java","adding found devices....");//working

                            }
                        });
                    }
                }
            };

}
