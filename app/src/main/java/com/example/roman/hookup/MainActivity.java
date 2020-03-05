package com.example.roman.hookup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


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

    /* If QR scanner scanned then do. */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent Result = new Intent(getApplicationContext(), ReceiveActivity.class);
                Result.putExtra("string", result.getContents());
                startActivity(Result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
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
        if (item.getItemId() == R.id.action_info) {
            Intent startInfo = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(startInfo);
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



