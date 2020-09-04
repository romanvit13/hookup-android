package com.vit.roman.hookup.activity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.roman.hookup.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.makeText;

public class ReceiveActivity extends AppCompatActivity {
    private TextView fullNameText;
    private TextView numberText;
    private TextView faceText;
    private TextView instagramText;

    @Override
    protected void onPause() {
        super.onPause();

        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        initEditTexts();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String info = bundle.getString(ShareActivity.USER_INFO);
            parse(info);
            setTitle(R.string.receive_label);
        } else {
            loadData();
            setTitle(R.string.receive_label1);
        }

        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        numberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", numberText.getText().toString());
                if (clipboard != null)
                    clipboard.setPrimaryClip(clip);
            }

        });

        faceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = faceText.getText().toString();
                makeText(ReceiveActivity.this, R.string.Facebook_open, Toast.LENGTH_SHORT).show();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + login));
                    startActivity(intent);
                }

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", login);
                if (clipboard != null)
                    clipboard.setPrimaryClip(clip);
            }
        });

        instagramText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = instagramText.getText().toString();
                makeText(ReceiveActivity.this, R.string.Instagram_open, Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("http://instagram.com/_u/" + login);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + login)));
                }

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", instagramText.getText().toString());
                if (clipboard != null)
                    clipboard.setPrimaryClip(clip);
            }
        });
    }

    public void parse(String info) {
        String[] array = new String[4];
        int i = 0;
        Pattern p = Pattern.compile(":(.*?);");
        Matcher m = p.matcher(info);

        while (m.find()) {
            if (m.group(1) != null) {
                array[i] = m.group(1);
            } else array[i] = "nothing";
            i++;
        }

        if (array[1] != null) {
            fullNameText.setText(array[0].replaceAll("\\s+", ""));
            numberText.setText(array[1].replaceAll("\\s+", ""));
            faceText.setText(array[2].replaceAll("\\s+", ""));
            instagramText.setText(array[3].replaceAll("\\s+", ""));
        } else {
            setTitle(R.string.receive_label3);
            Toast.makeText(ReceiveActivity.this, R.string.QR_error, Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_receive_error);
            EditText receivedText = findViewById(R.id.receivedText);
            receivedText.setText(info);
        }
    }

    private void initEditTexts() {
        fullNameText = findViewById(R.id.fullNameEdit);
        fullNameText.setBackground(null);
        numberText = findViewById(R.id.phoneNumberEdit);
        numberText.setBackground(null);
        numberText.setFocusable(false);
        faceText = findViewById(R.id.facebookEdit);
        faceText.setBackground(null);
        faceText.setFocusable(false);
        instagramText = findViewById(R.id.instaEdit);
        instagramText.setBackground(null);
        instagramText.setFocusable(false);
    }

    private void saveData() {
        SharedPreferences sharedPref = ReceiveActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(fullNameText.getId() + "", fullNameText.getText().toString());
        editor.putString(numberText.getId() + "", numberText.getText().toString());
        editor.putString(faceText.getId() + "", faceText.getText().toString());
        editor.putString(instagramText.getId() + "", instagramText.getText().toString());
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = ReceiveActivity.this.getPreferences(Context.MODE_PRIVATE);
        fullNameText.setText(sharedPref.getString(fullNameText.getId() + "", ""));
        numberText.setText(sharedPref.getString(numberText.getId() + "", ""));
        faceText.setText(sharedPref.getString(faceText.getId() + "", ""));
        instagramText.setText(sharedPref.getString(instagramText.getId() + "", ""));
    }
}

