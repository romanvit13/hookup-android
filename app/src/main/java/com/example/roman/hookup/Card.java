package com.example.roman.hookup;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private List<Field> mFields;

    public Card() {
        mFields = new ArrayList<>();
    }

    public void addField(Field field) {
        mFields.add(field);
    }

    public void removeField(Field field) {
        mFields.remove(field);
    }

    @Nullable
    public Field getField(FieldType fieldType) {
        for (Field field : mFields) {
            if (field.getFieldType() == fieldType)
                return field;
        }

        return null;
    }
}
