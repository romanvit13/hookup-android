package com.vit.roman.hookup.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.vit.roman.hookup.R;
import com.vit.roman.hookup.common.Card;
import com.vit.roman.hookup.common.CardsManager;
import com.vit.roman.hookup.common.Field;
import com.vit.roman.hookup.common.FieldType;

public class UserCardActivity extends AppCompatActivity {
    private EditText[] textViews;

    @Override
    protected void onStart() {
        super.onStart();

        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();

        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);
        setTitle(getResources().getString(R.string.share_activity_title));
        initTextViews();
        initButtons();
    }

    private void initTextViews() {
        /*List for Edit Texts*/
        textViews = new EditText[4];

        /*Initialize the list*/
        textViews[0] = findViewById(R.id.fullNameEdit);
        textViews[1] = findViewById(R.id.phoneNumberEdit);
        textViews[2] = findViewById(R.id.facebookEdit);
        textViews[3] = findViewById(R.id.instaEdit);
    }

    private void initButtons() {
        final Button saveInfoButton = findViewById(R.id.save_button);
        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveData() {
        Card userCard = new Card();
        userCard.addField(new Field(FieldType.fullname, textViews[0].getText().toString()));
        userCard.addField(new Field(FieldType.phone, textViews[1].getText().toString()));
        userCard.addField(new Field(FieldType.facebook, textViews[2].getText().toString()));
        userCard.addField(new Field(FieldType.instagram, textViews[3].getText().toString()));

        CardsManager.singleton.setUserCard(userCard);
    }

    private void loadData() {
        Card userCard = CardsManager.singleton.getUserCard();
        textViews[0].setText(userCard.getField(FieldType.fullname).getValue());
        textViews[1].setText(userCard.getField(FieldType.phone).getValue());
        textViews[2].setText(userCard.getField(FieldType.facebook).getValue());
        textViews[3].setText(userCard.getField(FieldType.instagram).getValue());
    }
}
