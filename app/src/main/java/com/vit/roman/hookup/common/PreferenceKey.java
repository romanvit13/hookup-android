package com.vit.roman.hookup.common;

public enum PreferenceKey {
    userCard("userCard"),
    cards("cards");

    private String mKey;

    PreferenceKey(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }
}
