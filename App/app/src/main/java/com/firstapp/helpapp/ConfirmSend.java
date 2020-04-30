package com.firstapp.helpapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmSend extends AppCompatActivity {

    Button sendBut;
    RadioButton freeRadio, paidRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_send_activity);
        Toast.makeText(this, "Letter saved", Toast.LENGTH_LONG).show();

        sendBut = findViewById(R.id.confrimButton);
        freeRadio = findViewById(R.id.freeRadioBut);
        paidRadio = findViewById(R.id.paidRadioBut);

        paidRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendBut.setEnabled(true);
            }
        });
        freeRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendBut.setEnabled(true);
            }
        });
    }


    public void sendButtonPressed(View view) {
        Intent intent = new Intent(this, Sent.class);
        startActivity(intent);
    }
}
