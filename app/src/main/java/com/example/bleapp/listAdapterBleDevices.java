package com.example.bleapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class listAdapterBleDevices extends BaseAdapter {

    //init array
    ArrayList<bleDevice> devices;
    int layoutResourceID;
    Activity activity;
    private LayoutInflater mInflator;

    //constructor
    public listAdapterBleDevices(Activity activity, int resource, ArrayList<bleDevice> objects) {
        super();
        this.activity = activity;
        layoutResourceID = resource;
        devices = objects;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //getting ble device attribute
    @Override
    public View getView(int i, View view,ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view =mInflator.inflate(R.layout.btle_device_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress= (TextView) view.findViewById(R.id.tv_macaddr);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.rssi=(TextView)view.findViewById(R.id.tv_rssi);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        bleDevice device = devices.get(i);
        String name = device.getName();
        String address = device.getAddress();
        int rssi = device.getRssi();

        if (name != null && name.length() > 0) {
            viewHolder.deviceName.setText(name);
        }
        else {
            viewHolder.deviceName.setText("unknown devices");
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
