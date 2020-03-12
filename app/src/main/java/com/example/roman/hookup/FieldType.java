package com.example.roman.hookup;

import androidx.annotation.NonNull;

public enum FieldType {
    fullname("fullName"),
    email("email"),
    phone("phone"),
    instagram("instagram"),
    facebook("facebook");

    private String value;

    FieldType(@NonNull String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
