package com.example.bleapp;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class leDeviceAdapter extends RecyclerView.Adapter<leDeviceAdapter.MyViewHolder> {

    private List<MainActivity.LeDeviceListAdapter> ledeviceLists;

    public leDeviceAdapter(List<MainActivity.LeDeviceListAdapter> ledeviceLists) {
        this.ledeviceLists = ledeviceLists;
    }

    @NonNull
    @Override
    public leDeviceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new leDeviceAdapter.MyViewHolder((View) ledeviceLists);
    }

    @Override
    public void onBindViewHolder(@NonNull leDeviceAdapter.MyViewHolder holder, int position) {
        MainActivity.LeDeviceListAdapter bdevice = ledeviceLists.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
