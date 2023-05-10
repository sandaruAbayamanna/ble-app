package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.mindrot.jbcrypt.BCrypt;

public class AuthActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView,devNameText,devAddrText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        // Initialize Firebase
        FirebaseApp.initializeApp(this);

     /*   // Get references to the EditText and Button
        mPasswordEdit = findViewById(R.id.password_edit);
        Button mAuthButton = findViewById(R.id.auth_button);

        // Set the device name in the TextView
        TextView deviceNameText = findViewById(R.id.device_name_text);

        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String deviceName = sharedPreferences.getString("device_name", "unknown Device");

        deviceNameText.setText(deviceName);

        //deviceNameText.setText(deviceName);
        Log.i("device name","Dev name is : "+deviceName);*/

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);
        devNameText = findViewById(R.id.devName);
        devAddrText=findViewById(R.id.devAddr);

        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String deviceName = sharedPreferences.getString("device_name", "unknown Device");
        String deviceAddress = sharedPreferences.getString("device_address", "Address Not found!!");


        devNameText.setText(deviceName);
        devAddrText.setText(deviceAddress);
        
        loginBtn.setOnClickListener(v -> loginUser());
        createAccountBtnTextView.setOnClickListener(v -> startActivity(new Intent(AuthActivity.this,CreateAccountActivity.class)));

    }

    public void loginUser() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }

        loginAccountInFirebase(email, password);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                changeInProgress(false);
                if (task.isSuccessful()){
                    //login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to next activity(note)
                        startActivity(new Intent(AuthActivity.this,ListNotesActivity.class));
                    }else {
                        //email not verified
                        Utils.toast(AuthActivity.this,"Email is not verified .please verify your email");
                    }
                }else {
                    //login failed
                    Utils.toast(AuthActivity.this,task.getException().getLocalizedMessage());

                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password){

        //validating the email format using patterns class
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        //validating the password format
        if (password.length()<6){
            passwordEditText.setError("password length is invalid");
            return false;
        }
        return true;

    }
}