package com.example.roman.hookup;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.makeText;

public class ReceiveActivity extends AppCompatActivity {

    private TextView fullNameText;
    private TextView numberText;
    private TextView faceText;
    private TextView instaText;

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        setTitle(R.string.receive_label);


        fullNameText = (TextView) findViewById(R.id.fullNameEdit);
        numberText = (TextView) findViewById(R.id.phoneNumberEdit);
        faceText = (TextView) findViewById(R.id.facebookEdit);
        instaText = (TextView) findViewById(R.id.instaEdit);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String info = bundle.getString("string");
            parse(info);
        } else {
            loadData();
            setTitle(R.string.receive_label1);
        }


        Button closeButton = (Button) findViewById(R.id.closeButton);
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
                clipboard.setPrimaryClip(clip);
            }
        });

        instaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = instaText.getText().toString();
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
                ClipData clip = ClipData.newPlainText("", instaText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

    }

    private HashMap<String, String> readJson() throws IOException, JSONException {
        fullNameText = (TextView) findViewById(R.id.fullNameEdit);
        numberText = (TextView) findViewById(R.id.phoneNumberEdit);
        faceText = (TextView) findViewById(R.id.facebookEdit);
        instaText = (TextView) findViewById(R.id.instaEdit);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getExternalFilesDir("saveJsonFolder");
        File jsonFile = new File(directory.toString(), "save.json");
        FileInputStream stream = new FileInputStream(jsonFile);
        String jsonStr = null;
        FileChannel fc = stream.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        jsonStr = Charset.defaultCharset().decode(bb).toString();
        JSONArray array = new JSONArray(jsonStr);

        HashMap<String, String> parsedData = new HashMap<>();

        JSONObject obj = array.getJSONObject(0);

        String firstName = obj.getString("full_name");
        fullNameText.setText(firstName);
        String number = obj.getString("number");
        numberText.setText(number);
        String faceLogin = obj.getString("facebook_login");
        faceText.setText(faceLogin);
        String instaLogin = obj.getString("insta_login");
        instaText.setText(instaLogin);


        parsedData.put("full_name", firstName);
        parsedData.put("number", number);
        parsedData.put("facebook_login", faceLogin);
        parsedData.put("insta_login", instaLogin);

        return parsedData;
    }


    public void parse(String info) {
        String[] array = new String[5];
        int i = 0;
        Pattern p = Pattern.compile("\\:(.*?)\\;");
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
            instaText.setText(array[3].replaceAll("\\s+", ""));
        } else {
            setTitle(R.string.receive_label3);
            Toast.makeText(ReceiveActivity.this, R.string.QR_error, Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_receive_error);
            EditText receivedText = (EditText) findViewById(R.id.receivedText);
            receivedText.setText(info);
        }
    }


    public int saveData() {
        SharedPreferences sharedPref = ReceiveActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(fullNameText.getId() + "", fullNameText.getText().toString());
        editor.putString(numberText.getId() + "", numberText.getText().toString());
        editor.putString(faceText.getId() + "", faceText.getText().toString());
        editor.putString(instaText.getId() + "", instaText.getText().toString());
        editor.commit();
        return 0;
    }

    private void loadData() {
        SharedPreferences sharedPref = ReceiveActivity.this.getPreferences(Context.MODE_PRIVATE);
        fullNameText.setText(sharedPref.getString(fullNameText.getId() + "", ""));
        numberText.setText(sharedPref.getString(numberText.getId() + "", ""));
        faceText.setText(sharedPref.getString(faceText.getId() + "", ""));
        instaText.setText(sharedPref.getString(instaText.getId() + "", ""));
    }
}

