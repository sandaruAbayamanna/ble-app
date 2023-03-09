package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView BluStatus ,DeviceList;
    ImageView iconB;
    Button onBtn,offBtn,pairedBtn,discoverableBtn;
     BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        BluStatus = findViewById(R.id.bluStatus);
        DeviceList = findViewById(R.id.listDv);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        pairedBtn = findViewById(R.id.pairedBtn);
        discoverableBtn = findViewById(R.id.discoverableBtn);

        //checking the bluetooth availability
        if (bluetoothAdapter ==null){
            BluStatus.setText("Bluetooth is not available");
        }else {
            BluStatus.setText("Bluetooth is available");
        }

        //setting up the bluetooth icon
        if (bluetoothAdapter.isEnabled()){
            iconB.setImageResource(R.drawable.ic_action_on);
        }else {
            iconB.setImageResource(R.drawable.ic_action_off);
        }



    }
}