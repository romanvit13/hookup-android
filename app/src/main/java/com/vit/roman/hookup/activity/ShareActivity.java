
package com.vit.roman.hookup.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vit.roman.hookup.R;
import com.vit.roman.hookup.common.Card;
import com.vit.roman.hookup.common.Field;
import com.vit.roman.hookup.common.FieldType;
import com.vit.roman.hookup.common.CardsManager;
import com.vit.roman.hookup.R;


public class ShareActivity extends AppCompatActivity {
    public static final String USER_INFO = "USER_INFO";
    private static final int CAMERA_PERMISSION = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    /* If QR scanner scanned then do */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null || result.getContents() == null) {
            super.onActivityResult(requestCode, resultCode, data);

            return;
        }

        Intent intent = new Intent(getApplicationContext(), ReceiveActivity.class);
        intent.putExtra(USER_INFO, result.getContents());
        startActivity(intent);
    }

    private void launchCodeScannerActivity() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    /* Create a menu with buttons on top */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            Intent startInfo = new Intent(ShareActivity.this, AppInfoActivity.class);
            startActivity(startInfo);
        }

        return true;
    }

    private void displayQrCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 812, 812);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            ((ImageView) findViewById(R.id.QRImageView)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private boolean hasCameraPermission() {
        return PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(Manifest.permission.CAMERA);
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCodeScannerActivity();
            }
        }
    }

    private void initButtons() {
        /*Three functional buttons.*/
        final Button historyButton = findViewById(R.id.historyButton);
        final Button receiveButton = findViewById(R.id.receiveButton);
        final Button editUserInfoButton = findViewById(R.id.edit_button);

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

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareActivity.this, ReceiveActivity.class));
            }
        });

        editUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, UserCardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        Card userCard = CardsManager.singleton.getUserCard();
        if (userCard == null) {
            return;
        }

        Field fullName = userCard.getField(FieldType.fullname);
        if (!fullName.getValue().isEmpty()) {
            final TextView userFullNameTv = findViewById(R.id.user_full_name);
            userFullNameTv.setText(fullName.getValue());
        }

        String userInfo =
                " Full name: " + fullName.getValue() + ";\n"
                        + " Phone number: " + userCard.getField(FieldType.phone).getValue() + ";\n"
                        + " Facebook login: " + userCard.getField(FieldType.facebook).getValue() + ";\n"
                        + " Instagram login: " + userCard.getField(FieldType.instagram).getValue() + ";";

        displayQrCode(userInfo);
    }
}



