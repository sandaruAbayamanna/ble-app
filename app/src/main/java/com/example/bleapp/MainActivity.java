package com.example.bleapp;

import static android.text.method.TextKeyListener.clear;

import static java.util.Collections.addAll;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 1;
    //init scanLeDevice class
    private scanLeDevice mScanLeDevice;

    TextView BluStatus;
    ImageView iconB;
    Button onBtn, offBtn, scannerBtn;

    private HashMap<String, bleDevice> mBleDevicesHashMap;
    private ArrayList<bleDevice> mBleDevicesArrayList;
    private ListView listView;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private final static String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;


    ///Requesting location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            //Do something based on grantResults
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission granted...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "coarse location permission granted");
            } else {
                Toast.makeText(this, "Location Permission denied...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "coarse location permission denied");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("activity", "OnCreate");


        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        BluStatus = findViewById(R.id.bluStatus);
        iconB = findViewById(R.id.iconBlu);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scannerBtn = findViewById(R.id.scannerBtn);

        //create new objects
        mBleDevicesHashMap = new HashMap<>();
        mBleDevicesArrayList = new ArrayList<>();

        mScanLeDevice = new scanLeDevice(this, 7500, -75);

        //checking the bluetooth availability
        if (bluetoothAdapter == null) {
            BluStatus.setText(R.string.bl_unavailable);
        } else {
            BluStatus.setText(R.string.bl_available);
        }

        //setting up the bluetooth icon
        if (bluetoothAdapter.isEnabled()) {
            iconB.setImageResource(R.drawable.bon);
        } else {
            iconB.setImageResource(R.drawable.bdeactivate);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("activity", "OnStart");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("activity", "OnResume");
        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show the custom dialog with a timeout of 2 seconds (adjust the duration as needed)
                showCustomDialogWithTimeout(2000);

                //set notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "MyNotification");
                builder.setContentTitle("Scanning");
                builder.setContentText("ROOKIE Scanning The BLE Devices ");
                builder.setSmallIcon(R.drawable.icon_launch_foreground);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.scann_icon));
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerCompat.notify(1, builder.build());


                //start the scan
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!mScanLeDevice.isScanning()) {
                        startScan();
                        Log.i("MainActivity.java","scan method start through btn pressed!!!!!!!");
                    }
                    else {
                        stopScan();
                    }
                }

                Log.i("btn","scanner btn called");
                List<bleDevice> devList= new ArrayList<>();

                //adding lists
                //how should add????
                for (bleDevice device : mBleDevicesArrayList){
                    devList.add(device);
                    Log.i("dev List","adding to the list view today##");
                }

                Log.i("onResume","outside the device add method ");
                listAdapterBleDevices adapter = new listAdapterBleDevices(getApplicationContext(), (ArrayList<bleDevice>) devList);
                listView.setAdapter(adapter);
                Log.i("adapter",""+adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //get position of the item from the list
                        bleDevice item = (bleDevice) parent.getItemAtPosition(position);
                        //Toast.makeText(MainActivity.this, item.getAddress(), Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(currentUser==null){
                                    startActivity(new Intent(MainActivity.this,AuthActivity.class));
                                }else{
                                    startActivity(new Intent(MainActivity.this,ListNotesActivity.class));
                                }
                                finish();
                            }
                        },1000);

                        //using shared preferences to store device name& address
                        SharedPreferences sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("device_name", item.getName());
                        editor.putString("device_address", item.getAddress());
                        editor.apply();
                    }
                });
            }
        });

    }

    public void addDevice(BluetoothDevice device, int rssi) {

        /*if (!mBleDevicesArrayList.contains(device)){
            bleDevice btleDevice = new bleDevice(device);
            mBleDevicesArrayList.add(btleDevice);
        }*/
        /*//Log.i("in the adapter class","getting results to the add method" +device);//ok
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

        //adapter.notifyDataSetChanged();*/

        String address = device.getAddress();

        if (!mBleDevicesHashMap.containsKey(address)) {
            bleDevice btleDevice = new bleDevice(device);
            btleDevice.setRssi(rssi);

            mBleDevicesHashMap.put(address, btleDevice);
            mBleDevicesArrayList.add(btleDevice);

            listAdapterBleDevices adapter = (listAdapterBleDevices) listView.getAdapter();
            if (adapter != null) {
                adapter.updateData(mBleDevicesArrayList);
            }
        } else {
            Objects.requireNonNull(mBleDevicesHashMap.get(address)).setRssi(rssi);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScan(){

        mBleDevicesArrayList.clear();
        mBleDevicesHashMap.clear();
       // adapter.notifyDataSetChanged();

        mScanLeDevice.start();
        Log.i("mainActivity.java","start scanning method called");



    }
    public void stopScan() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    private void showCustomDialogWithTimeout(long duration) {
        // Create the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progressbar_dialog, null);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        TextView messageTextView = dialogView.findViewById(R.id.textView5);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();

        // Set a timeout to automatically dismiss the dialog
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Dismiss the dialog
                dialog.dismiss();
            }
        }, duration);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();//clear all the activities  in the backStack
                        System.exit(0);
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}