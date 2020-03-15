package com.vit.roman.hookup.common;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public enum CardsManager {
    singleton;

    private static final String APP_PREFERENCES = "preferences";

    Card mUserCard;
    List<Card> mCards;

    SharedPreferences mPreferences;

    CardsManager() {
        mUserCard = new Card();
        mCards = new ArrayList<>();
        mPreferences = App.getAppContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        loadUserCard();
    }

    public Card getUserCard() {
        return mUserCard;
    }

    public void setUserCard(@NonNull Card userCard) {
        mUserCard = userCard;
        persistUserCard();
    }

    private void persistUserCard() {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String cardJson = gson.toJson(mUserCard);
        editor.putString(PreferenceKey.userCard.getKey(), cardJson);
        editor.apply();
    }

    private void loadUserCard() {
        Gson gson = new Gson();

        String userCard = mPreferences.getString(PreferenceKey.userCard.getKey(), null);
        if (userCard == null) {
            return;
        }

        mUserCard = gson.fromJson(userCard, Card.class);
    }
}
