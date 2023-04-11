package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        // Get the device address from the Intent
        String deviceAddress = getIntent().getStringExtra(DEVICE_ADDRESS);

        // Get the BluetoothDevice for the selected address
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        mDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

        /*// Set the device name in the TextView
        TextView deviceNameText = findViewById(R.id.device_name_text);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mDevice.getName() != null) {
            deviceNameText.setText(mDevice.getName());
        }
        else {
            deviceNameText.setText(R.string.unkDev);}

        deviceNameText.setText(mDevice.getName());*/

        // Set a click listener on the Button
        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("authButton","login btn clicked");
                // Get the password from the EditText
                String password = mPasswordEdit.getText().toString();

                Utils.toast(getApplicationContext(), "Login Button Pressed");
                Log.i("authButton","login btn clicked");

                // Start the authentication process
                authenticate(password);
            }
        });
    }

    private void authenticate(String password){
        Log.i("auth","authentication started");
    }

}