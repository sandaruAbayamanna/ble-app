package com.example.bleapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class listAdapterBleDevices extends ArrayAdapter<scanLeDevice> {

    ArrayList<scanLeDevice> devices;

    public listAdapterBleDevices(@NonNull Context context, int resource, ArrayList<scanLeDevice> devices) {
        super(context, resource);
        this.devices = devices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
