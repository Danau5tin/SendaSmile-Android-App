package com.firstapp.helpapp;

import android.app.Activity;
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

        freeRadio = findViewById(R.id.freeRadioBut);
        paidRadio = findViewById(R.id.paidRadioBut);

        paidRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","paid");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        freeRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","free");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    public void sendButtonPressed(View view) {
        Intent intent = new Intent(this, Sent.class);
        startActivity(intent);
    }
}
