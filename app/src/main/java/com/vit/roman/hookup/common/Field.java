package com.vit.roman.hookup.common;

public class Field {
    private FieldType mFieldType;
    private String mValue;

    public Field(FieldType fieldType, String value) {
        mFieldType = fieldType;
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public FieldType getFieldType() {
        return mFieldType;
    }
}
