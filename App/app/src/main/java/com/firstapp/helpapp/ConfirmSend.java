package com.firstapp.helpapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmSend extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_send_activity);
    }


    public void sendButtonPressed(View view) {
        Intent intent = new Intent(this, Sent.class);
        startActivity(intent);
    }
}
