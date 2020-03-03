package com.example.roman.hookup;

import android.content.ContextWrapper;
import android.widget.TextView;

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

public class Utils {

//    private int firstRun() {
//        final String PREFS_NAME = "MyPrefsFile";
//        final String PREF_VERSION_CODE_KEY = "version_code";
//        final int DOESNT_EXIST = -1;
//
//        int result = 0;
//
//        int currentVersionCode = BuildConfig.VERSION_CODE;
//
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
//        if (currentVersionCode == savedVersionCode) {
//            result = 0;
//        } else if (savedVersionCode == DOESNT_EXIST) {
//            result = 1;
//        } else if (currentVersionCode > savedVersionCode) {
//            result = 1;
//        }
//        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
//        return result;
//    }
//
//    /*Will use it later.*/
//    public JSONArray makeJSON() {
//        JSONArray jArr = new JSONArray();
//        JSONObject jObj = new JSONObject();
//        try {
//
//            jObj.put("full_name", ((EditText) findViewById(R.id.fullNameEdit)).getText().toString());
//            jObj.put("number", ((EditText) findViewById(R.id.phoneNumberEdit)).getText().toString());
//            jObj.put("facebook_login", ((EditText) findViewById(R.id.facebookEdit)).getText().toString());
//            jObj.put("insta_login", ((EditText) findViewById(R.id.instaEdit)).getText().toString());
//
//            jArr.put(jObj);
//
//        } catch (Exception e) {
//            System.out.println("Error:" + e);
//        }
//
//        return jArr;
//    }
//
//    /*Will use it later.*/
//    private void saveJson() {
//
//        try {
//            ContextWrapper cw = new ContextWrapper(getApplicationContext());
//            File directory = cw.getExternalFilesDir("saveJsonFolder");
//            Writer output = null;
//            File file = new File(directory.toString(), "save.json");
//            output = new BufferedWriter(new FileWriter(file));
//            output.write(makeJSON().toString());
//            output.close();
//            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
//
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
//    private HashMap<String, String> readJson() throws IOException, JSONException {
//        fullNameText = (TextView) findViewById(R.id.fullNameEdit);
//        numberText = (TextView) findViewById(R.id.phoneNumberEdit);
//        faceText = (TextView) findViewById(R.id.facebookEdit);
//        instagramText = (TextView) findViewById(R.id.instaEdit);
//
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getExternalFilesDir("saveJsonFolder");
//        File jsonFile = new File(directory.toString(), "save.json");
//        FileInputStream stream = new FileInputStream(jsonFile);
//        String jsonStr = null;
//        FileChannel fc = stream.getChannel();
//        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//        jsonStr = Charset.defaultCharset().decode(bb).toString();
//        JSONArray array = new JSONArray(jsonStr);
//
//        HashMap<String, String> parsedData = new HashMap<>();
//
//        JSONObject obj = array.getJSONObject(0);
//
//        String firstName = obj.getString("full_name");
//        fullNameText.setText(firstName);
//        String number = obj.getString("number");
//        numberText.setText(number);
//        String faceLogin = obj.getString("facebook_login");
//        faceText.setText(faceLogin);
//        String instaLogin = obj.getString("insta_login");
//        instagramText.setText(instaLogin);
//
//
//        parsedData.put("full_name", firstName);
//        parsedData.put("number", number);
//        parsedData.put("facebook_login", faceLogin);
//        parsedData.put("insta_login", instaLogin);
//
//        return parsedData;
//    }
}
