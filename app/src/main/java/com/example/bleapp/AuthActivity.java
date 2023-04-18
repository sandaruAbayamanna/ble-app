package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class AuthActivity extends AppCompatActivity {

    //get the passed device name & address
    static String DEVICE_NAME = "DEVICE_NAME";
    static String DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private BluetoothDevice mDevice;
    private BluetoothGatt mGatt;
    private EditText mPasswordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Get references to the EditText and Button
        mPasswordEdit = findViewById(R.id.password_edit);
        Button mAuthButton = findViewById(R.id.auth_button);

        // Get the device address & Name from the Intent
        String deviceAddress = getIntent().getStringExtra(DEVICE_ADDRESS);
        String deviceName = getIntent().getStringExtra(DEVICE_NAME);

        // Get the BluetoothDevice for the selected address
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        mDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

        // Set the device name in the TextView
        TextView deviceNameText = findViewById(R.id.device_name_text);

       /* if (mDevice != null) {
            deviceNameText.setText(deviceName);
        }
        else {
            deviceNameText.setText(R.string.unkDev);}*/

        deviceNameText.setText(deviceAddress);

        // Set a click listener on the Button
        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("authButton", "login btn clicked");
                // Get the password from the EditText
                String password = mPasswordEdit.getText().toString();

                Utils.toast(getApplicationContext(), "Login Button Pressed");
                Log.i("authButton", "login btn clicked");

                // Start the authentication process
                authenticate(password);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void authenticate(String password) {
        Log.i("auth", "authentication started");

        // Connect to the device
        //=>  This connects to the GATT server hosted by the BLE device
        //=> and returns a BluetoothGatt instance, which you can then use to conduct GATT client operations
        // This method takes three parameters: a Context object, autoConnect and a reference to a BluetoothGattCallback:

        if (password.equals("admin")){
            startActivity(new Intent(AuthActivity.this,DevHomeActivity.class));
        }else{
            Toast.makeText(this, "Password is invalid please try again !!!!!", Toast.LENGTH_SHORT).show();
        }

        /*mGatt = mDevice.connectGatt(this, false, new BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // Discover services on the device
                    //when discoverServices is completed onServicesDiscovered method is called
                    gatt.discoverServices();
                    Log.i("connectGatt","discovering services");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // Close the GATT connection
                    gatt.close();
                }

            }

            @SuppressLint("MissingPermission")
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // Get the service and characteristic for the authentication process
                    BluetoothGattService authService = gatt.getService(UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"));
                    BluetoothGattCharacteristic authChar = authService.getCharacteristic(UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb"));

                    // Authenticate with the device by writing the password to the characteristic
                    authChar.setValue(password.getBytes());
                    gatt.writeCharacteristic(authChar);
                } else {
                    // Close the GATT connection
                    gatt.close();
                }
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {

                    Log.i("GATT","Authentication Success");


                    // Authentication was successful, close the GATT connection
                    gatt.close();
                } else {
                    // Authentication failed, close the GATT connection
                    Log.i("GATT","Authentication failed");
                    gatt.close();

                }
            }
        });*/

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the GATT connection if it's still open
        if (mGatt != null) {

            mGatt.close();
        }
    }
}