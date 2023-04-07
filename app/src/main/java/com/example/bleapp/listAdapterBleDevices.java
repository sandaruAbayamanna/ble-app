package com.example.bleapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//Adapter for holding the scanned devices
public class listAdapterBleDevices extends BaseAdapter {

    //define the array
    private static HashMap<String, bleDevice> mBleDevicesHashMap;
    private static ArrayList<bleDevice> mBleDevicesArrayList;
    private final Context context;
    private LayoutInflater mInflator;


    //constructor
    @SuppressLint("NewApi")
    public listAdapterBleDevices(Context context, ArrayList<bleDevice> mBleDevicesArrayList) {
        super();
       this.context = context;
        listAdapterBleDevices.mBleDevicesArrayList = mBleDevicesArrayList;


    }

    @Override
    public int getCount() {
        return mBleDevicesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBleDevicesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public static void addDevice(BluetoothDevice device, int rssi) {
        //Log.i("in the adapter class","getting results to the add method" +device);//ok
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

        //adapter.notifyDataSetChanged();
    }

    //getting ble device attribute
    @Override
    public View getView(int i, View view,ViewGroup viewGroup) {
        Log.i("view","view called");//not logging
        ViewHolder viewHolder;
        if (view == null) {
            Log.i("view holder","view holder is null");
            //inflate the layout
            view = LayoutInflater.from(context).inflate(R.layout.btle_device_list_item, null);
            //set up the viewHolder
            viewHolder = new ViewHolder();
            Log.i("listAdapterbleDevices","creating viewHolder");//not logging
            viewHolder.deviceAddress= (TextView) view.findViewById(R.id.tv_macaddr);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.rssi=(TextView)view.findViewById(R.id.tv_rssi);
            view.setTag(viewHolder);
        }else {
            //use the view holder
            viewHolder = (ViewHolder) view.getTag();
        }



        bleDevice device = mBleDevicesArrayList.get(i);
        String name = device.getName();
        String address = device.getAddress();
        int rssi = device.getRssi();

        if (name != null && name.length() > 0) {
            viewHolder.deviceName.setText(name);
        }
        else {
            viewHolder.deviceName.setText(R.string.unkDev);
            viewHolder.deviceAddress.setText(device.getAddress());
            viewHolder.rssi.setText((rssi));
        }

        return view;
    }

    static class ViewHolder{
        TextView deviceName;
        TextView deviceAddress;
        TextView rssi;
    }
}
