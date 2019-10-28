package com.example.roman.hookup;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle bundle = getIntent().getExtras();
        String QRContent = " Full name: " + bundle.getString("full name") + ";\n"
                + " Phone number: " + bundle.getString("number") + ";\n"
                + " Facebook login: " + bundle.getString("facebook") + ";\n"
                + " Instagram login: " + bundle.getString("insta") + ";\n"
                + " VK login: " + bundle.getString("vk") + ";";
        generateQR(QRContent);
    }


    /*Generate QR code method.*/
    private void generateQR(String content) {
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
