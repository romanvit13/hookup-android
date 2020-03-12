package com.example.roman.hookup;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Calendar;

public class App extends Application {
    private static final String APP_PREFERENCES = "preferences";

    public Context getApplicationContext() {
        return this;
    }
}
