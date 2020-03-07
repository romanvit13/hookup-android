package com.example.roman.hookup;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitle(getResources().getString(R.string.share_activity_title));

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.i("ShareActivity", "Intent bundle is null");
            return;
        }

        final TextView userFullNameTv = findViewById(R.id.user_full_name);
        userFullNameTv.setText(bundle.getString("full name"));

        String userInfo =
                " Full name: " + bundle.getString("full name") + ";\n"
                + " Phone number: " + bundle.getString("number") + ";\n"
                + " Facebook login: " + bundle.getString("facebook") + ";\n"
                + " Instagram login: " + bundle.getString("insta") + ";";

        displayQrCode(userInfo);
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

}
