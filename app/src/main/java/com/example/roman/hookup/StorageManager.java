package com.example.roman.hookup;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.List;

public enum StorageManager {
    singleton;

    Card mUserCard;
    List<Card> mCards;

    SharedPreferences mPreferences;

    StorageManager() {}

    public void persistUserCard(@NonNull Card userCard) {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String cardJson = gson.toJson(userCard);
        editor.putString(PreferenceKey.userCard.getKey(), cardJson);
        editor.apply();
    }

    @Nullable
    public Card loadUserCard() {
        Gson gson = new Gson();
        String userCardJson = mPreferences.getString(PreferenceKey.userCard.getKey(), null);
        if (userCardJson == null) {
            return null;
        }

        return gson.fromJson(userCardJson, Card.class);
    }
}
