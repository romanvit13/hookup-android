package com.example.roman.hookup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class UserCardActivity extends AppCompatActivity {
    private EditText[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitle(getResources().getString(R.string.share_activity_title));

        /*List for Edit Texts*/
        textViews = new EditText[4];

        /*Initialize the list*/
        textViews[0] = findViewById(R.id.fullNameEdit);
        textViews[1] = findViewById(R.id.phoneNumberEdit);
        textViews[2] = findViewById(R.id.facebookEdit);
        textViews[3] = findViewById(R.id.instaEdit);


        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.i("UserCardActivity", "Intent bundle is null");
            return;
        }

        String fullName = bundle.getString("full name");
        if (fullName != null && !fullName.isEmpty()) {
            final TextView userFullNameTv = findViewById(R.id.user_full_name);
            userFullNameTv.setText(fullName);
        }

        String userInfo =
                " Full name: " + bundle.getString("full name") + ";\n"
                + " Phone number: " + bundle.getString("number") + ";\n"
                + " Facebook login: " + bundle.getString("facebook") + ";\n"
                + " Instagram login: " + bundle.getString("insta") + ";";
    }
}
