package com.example.bleapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 1;
    //init scanLeDevice class
    private scanLeDevice mScanLeDevice;

    TextView BluStatus, DeviceList;
    ImageView iconB;
    Button onBtn, offBtn, scannerBtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        BluStatus = findViewById(R.id.bluStatus);
        DeviceList = findViewById(R.id.listDv);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scannerBtn = findViewById(R.id.scannerBtn);


        //checking the bluetooth availability
        if (bluetoothAdapter == null) {
            BluStatus.setText("Bluetooth is not available");
        } else {
            BluStatus.setText("Bluetooth is available");
        }

        //setting up the bluetooth icon
        if (bluetoothAdapter.isEnabled()) {
            iconB.setImageResource(R.drawable.ic_action_on);
        } else {
            iconB.setImageResource(R.drawable.ic_action_off);
        }

        //on button
        onBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Turning On Bluetooth", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth Is Already ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //off button
        offBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled()) {

                    bluetoothAdapter.disable();
                    Toast.makeText(MainActivity.this, "Turning Bluetooth OFF.....", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth Is Already OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //bluetooth scanning button
        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                if(bluetoothAdapter.isEnabled()){
                    DeviceList.setText("Available Devices");
                    @SuppressLint("MissingPermission")
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for (BluetoothDevice device:devices){
                        DeviceList.append("\nDevice" +device.getName()+"," +device);
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please turn on bluetooth to scan devices", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }

    public void addDevice(BluetoothDevice device, int new_rssi) {

    }

    public void stopScan() {

    }
}