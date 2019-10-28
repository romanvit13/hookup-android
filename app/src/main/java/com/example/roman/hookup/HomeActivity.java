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

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


public class HomeActivity extends AppCompatActivity {
    private static final int PERMS_REQUEST_CODE = 123;
    private EditText[] mEditTexts;
    private ShowcaseView mTutorialView;

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initEditTexts();
        initButtons();

        /*Load saved data*/
        loadData();
        requestSavePermission();
    }

    private void initEditTexts() {
        /*List for Edit Texts*/
        mEditTexts = new EditText[4];

        /*Initialize the list*/
        mEditTexts[0] = (EditText) findViewById(R.id.fullNameEdit);
        mEditTexts[1] = (EditText) findViewById(R.id.phoneNumberEdit);
        mEditTexts[2] = (EditText) findViewById(R.id.facebookEdit);
        mEditTexts[3] = (EditText) findViewById(R.id.instaEdit);
    }

    private void initButtons() {
        /*Three functional buttons.*/
        final Button historyButton = (Button) findViewById(R.id.historyButton);
        final Button shareButton = (Button) findViewById(R.id.shareToActivityButton);
        final Button receiveButton = (Button) findViewById(R.id.receiveButton);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReceiveActivity.class);
                startActivity(intent);
            }
        });


        /*Start share activity and pass some info to it.*/
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openShareActivity = new Intent(HomeActivity.this, ShareActivity.class);
                openShareActivity.putExtra("full name", mEditTexts[0].getText().toString());
                openShareActivity.putExtra("number", mEditTexts[1].getText().toString());
                openShareActivity.putExtra("facebook", mEditTexts[2].getText().toString());
                openShareActivity.putExtra("insta", mEditTexts[3].getText().toString());
                startActivity(openShareActivity);
            }
        });

        /*Start activity QR scanner.*/
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasCameraPermission()) {
                    Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                    intent.setAction("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SAVE_HISTORY", false);
                    startActivityForResult(intent, 0);
                } else {
                    requestCameraPermission();
                }
            }
        });
    }

    /**
     * Method on QR scanning result.
     * @param requestCode
     * @param resultCode - if it is 'OK', method populates an intent
     * @param intent - contains a QR code data in it
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Intent Result = new Intent(getApplicationContext(), ReceiveActivity.class);
                Result.putExtra("string", contents);
                startActivity(Result);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(HomeActivity.this, R.string.QR_error, Toast.LENGTH_SHORT).show();
                // Handle cancel
            }
        }
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
            case R.id.action_cart:
                Intent startInfo = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(startInfo);
                break;
        }
        return true;
    }


    public void saveData() {
        SharedPreferences sharedPref = HomeActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (EditText mEditText : mEditTexts) {
            if (
                    !mEditText.getText().toString().equals("") &&
                            !mEditText.getText().toString().equals("") &&
                            !mEditText.getText().toString().equals("")
            ) {
                editor.putString(Integer.toString(mEditText.getId()), mEditText.getText().toString());
            }
        }
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = HomeActivity.this.getPreferences(Context.MODE_PRIVATE);
        for (EditText mEditText : mEditTexts) {
            String saved = sharedPref.getString(Integer.toString(mEditText.getId()), "");
            if (!saved.equals("") && !saved.equals(" ") && !saved.equals("  ")) {
                mEditText.setText(saved);
            }
        }
    }

    /*Check if device allows to use camera.*/
    private boolean isHasCameraPermission() {
        int res;
        String permission = Manifest.permission.CAMERA;
        res = checkCallingOrSelfPermission(permission);
        return PackageManager.PERMISSION_GRANTED == res;
    }

    /*Request the save permission.*/
    private void requestSavePermission() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        }
    }

    /*Request the camera permission.*/
    private void requestCameraPermission() {
        String[] permissions = new String[]{
                Manifest.permission.CAMERA};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        }
    }

    private int firstRun() {
        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        int result = 0;

        int currentVersionCode = BuildConfig.VERSION_CODE;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        if (currentVersionCode == savedVersionCode) {
            result = 0;
        } else if (savedVersionCode == DOESNT_EXIST) {
            result = 1;
        } else if (currentVersionCode > savedVersionCode) {
            result = 1;
        }
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        return result;
    }

    private void firstGuide(){
        final ViewTarget target1 = new ViewTarget(R.id.fullNameEdit, this);
        final ViewTarget target2 = new ViewTarget(R.id.phoneNumberEdit, this);
        final ViewTarget target3 = new ViewTarget(R.id.facebookEdit, this);
        final ViewTarget target4 = new ViewTarget(R.id.shareToActivityButton, this);
        final ViewTarget target5 = new ViewTarget(R.id.receiveButton, this);


        mTutorialView = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(target1)
                .setContentTitle("Write here your name")
                .setStyle(R.style.CustomShowcaseMaterial)
                .build();

        mTutorialView.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        mTutorialView.setTarget(target2);
                        mTutorialView.setContentTitle("Write here your phone number");
                        mTutorialView.setButtonText("next");
                        break;

                    case 2:
                        mTutorialView.setTarget(target3);
                        mTutorialView.setContentTitle("Write here your login of facebook or instagram");
                        mTutorialView.setButtonText("next");
                        break;
                    case 3:
                        mTutorialView.setTarget(target4);
                        mTutorialView.setContentTitle("Tap this button to share your contacts");
                        mTutorialView.setButtonText("next");
                        break;
                    case 4:
                        mTutorialView.setTarget(target5);
                        mTutorialView.setContentTitle("Tap this button to get info from your friend");
                        mTutorialView.setButtonText("next");
                        break;
                    case 5:
                        mTutorialView.hide();
                        break;
                }
            }
        });
    }

    /*Will use it later.*/
    public JSONArray makeJSON() {
        JSONArray jArr = new JSONArray();
        JSONObject jObj = new JSONObject();
        try {

            jObj.put("full_name", ((EditText) findViewById(R.id.fullNameEdit)).getText().toString());
            jObj.put("number", ((EditText) findViewById(R.id.phoneNumberEdit)).getText().toString());
            jObj.put("facebook_login", ((EditText) findViewById(R.id.facebookEdit)).getText().toString());
            jObj.put("insta_login", ((EditText) findViewById(R.id.instaEdit)).getText().toString());

            jArr.put(jObj);

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }

        return jArr;
    }

    /*Will use it later.*/
    private void saveJson() {
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getExternalFilesDir("saveJsonFolder");
            Writer output = null;
            File file = new File(directory.toString(), "save.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(makeJSON().toString());
            output.close();
            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}



