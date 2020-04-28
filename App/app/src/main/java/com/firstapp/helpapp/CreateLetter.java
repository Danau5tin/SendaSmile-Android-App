package com.firstapp.helpapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateLetter extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_letter_activity);

    }

    public void createContinueButton(View view) {
        Intent intent = new Intent(this, ConfirmSend.class);
        startActivity(intent);
    }

    public void userInfoButton(View view) {
        Intent intent = new Intent(this, UserInfo.class);
        startActivity(intent);
    }
}
