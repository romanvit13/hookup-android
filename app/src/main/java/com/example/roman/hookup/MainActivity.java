package com.example.roman.hookup;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


public class MainActivity extends AppCompatActivity {
    private static final int PERMS_REQUEST_CODE = 123;
    private EditText[] textViews;

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*List for Edit Texts*/
        textViews = new EditText[4];

        /*Initialize the list*/
        textViews[0] = findViewById(R.id.fullNameEdit);
        textViews[1] = findViewById(R.id.phoneNumberEdit);
        textViews[2] = findViewById(R.id.facebookEdit);
        textViews[3] = findViewById(R.id.instaEdit);

        /*Three functional buttons.*/
        final Button historyButton = findViewById(R.id.historyButton);
        final Button shareButton = findViewById(R.id.shareToActivityButton);
        final Button receiveButton = findViewById(R.id.receiveButton);

        /*Load saved data*/
        loadData();

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReceiveActivity.class));
            }
        });

        /*Start share activity and pass some info to it.*/
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchShareActivity();
            }
        });

        /*Start activity QR scanner.*/
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    launchCodeScannerActivity();
                } else {
                    requestCameraPermission();
                }
            }
        });
    }

    /*If QR scanner scanned then do.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Intent Result = new Intent(getApplicationContext(), ReceiveActivity.class);
                Result.putExtra("string", contents);
                startActivity(Result);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, R.string.QR_error, Toast.LENGTH_SHORT).show();
                // Handle cancel
            }
        }
    }

    private void launchShareActivity() {
        Intent openShareActivity = new Intent(MainActivity.this, ShareActivity.class);
        openShareActivity.putExtra("full name", textViews[0].getText().toString());
        openShareActivity.putExtra("number", textViews[1].getText().toString());
        openShareActivity.putExtra("facebook", textViews[2].getText().toString());
        openShareActivity.putExtra("insta", textViews[3].getText().toString());
        startActivity(openShareActivity);
    }

    private void launchCodeScannerActivity() {
        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);
    }

    /*Create menu with buttons on top.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent startInfo = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(startInfo);
                break;
        }
        return true;
    }

    public void saveData() {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (EditText textView : textViews) {
            if ((!textView.getText().toString().equals(""))
                    && (!textView.getText().toString().equals(" "))
                    && (!textView.getText().toString().equals("  "))) {
                editor.putString(textView.getId() + "", textView.getText().toString());
            }
        }

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        for (EditText textView : textViews) {
            String saved = sharedPref.getString(textView.getId() + "", "");
            assert saved != null;
            if (!saved.equals("") && !saved.equals(" ") && !saved.equals("  ")) {
                textView.setText(saved);
            }
        }
    }

    /*Check if device allows to use camera.*/
    private boolean hasCameraPermission() {
        int res;
        String permission = Manifest.permission.CAMERA;
        res = checkCallingOrSelfPermission(permission);
        return PackageManager.PERMISSION_GRANTED == res;
    }

    /*Request the camera permission.*/
    private void requestCameraPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        }
    }
}



