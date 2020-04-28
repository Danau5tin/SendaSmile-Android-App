package com.firstapp.helpapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class PreLetterCreation extends AppCompatActivity {

    Switch anonSwitch;
    TextView usernameText, usernumText, anonDesc;
    EditText usernameEdit, usernumEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_letter_creation);

        usernameEdit = findViewById(R.id.userNameEdit);
        usernumEdit = findViewById(R.id.userNumEdit);
        usernameText = findViewById(R.id.userNameText);
        usernumText = findViewById(R.id.userNumText);
        anonDesc = findViewById(R.id.textAnon);

        anonSwitch = findViewById(R.id.switchAnon);
        anonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hide UI elements for user info
                    usernameEdit.setVisibility(View.INVISIBLE);
                    usernameText.setVisibility(View.INVISIBLE);
                    usernumEdit.setVisibility(View.INVISIBLE);
                    usernumText.setVisibility(View.INVISIBLE);
                    anonSwitch.setText(R.string.Yes);
                    anonDesc.setText(R.string.anon_expl);
                } else {
                    // Show UI elements for user info
                    usernameEdit.setVisibility(View.VISIBLE);
                    usernameText.setVisibility(View.VISIBLE);
                    usernumEdit.setVisibility(View.VISIBLE);
                    usernumText.setVisibility(View.VISIBLE);
                    anonSwitch.setText(R.string.No);
                    anonDesc.setText(R.string.non_anon_expl);
                }
            }
        });
    }

    public void preContinueButton(View view) {
        Intent intent = new Intent(this, CreateLetter.class);
        startActivity(intent);
    }
}
