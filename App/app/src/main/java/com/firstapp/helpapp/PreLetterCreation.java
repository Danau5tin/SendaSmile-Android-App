package com.firstapp.helpapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;


public class PreLetterCreation extends AppCompatActivity {

    Switch specAreaSwitch;
    Spinner destinationSpinner;
    TextView recipientText, genderText, specAreaText;
    RadioButton nhsCheck, elderlyCheck, gentlemanCheck, ladyCheck;
    RadioGroup genderRadGroup;
    Boolean nhsChecked, ladyChecked, recipChosen;
    ImageView nurseImg, doctorImg, grandmaImg, grandpaImg;
    InputStream imageStream;
    Button continueButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_letter_creation);


        genderRadGroup = findViewById(R.id.genderRadioGroup);
        genderText = findViewById(R.id.genderText);
        nhsCheck = findViewById(R.id.nhsCheckbox);
        elderlyCheck = findViewById(R.id.elderlyCheckbox);
        gentlemanCheck = findViewById(R.id.gentlemanCheckbox);
        ladyCheck = findViewById(R.id.ladyCheckbox);
        recipientText = findViewById(R.id.textRecipientDescr);
        specAreaText = findViewById(R.id.specAreaText);
        specAreaSwitch = findViewById(R.id.specAreaSwitch);
        nurseImg = findViewById(R.id.nurseImg);
        doctorImg = findViewById(R.id.doctorImg);
        grandmaImg = findViewById(R.id.grandmaImg);
        grandpaImg = findViewById(R.id.grandpaImg);

        destinationSpinner = findViewById(R.id.destinationSpinner);
        continueButton = findViewById(R.id.preCreateButton);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.uk_areas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(adapter);
        if (!specAreaSwitch.isChecked()) {destinationSpinner.setVisibility(View.GONE);}

        specAreaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {destinationSpinner.setVisibility(View.VISIBLE); specAreaSwitch.setText(R.string.Yes);}
                else {destinationSpinner.setVisibility(View.GONE); specAreaSwitch.setText(R.string.No);}}});


        recipChosen = false;
        nhsChecked = false;
        ladyChecked = false;


        nhsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nhsChecked = true;
                    recipientListener();
                        genderRadGroup.setVisibility(View.VISIBLE);
                        genderText.setVisibility(View.VISIBLE);
                    }}});

        elderlyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nhsChecked = false;
                    recipientListener();
                    genderRadGroup.setVisibility(View.VISIBLE);
                    genderText.setVisibility(View.VISIBLE);
                        recipientListener();}}});

        ladyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ladyChecked = true;
                    recipChosen = true;
                    recipientText.setVisibility(View.VISIBLE);
                    specAreaText.setVisibility(View.VISIBLE);
                    specAreaSwitch.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.VISIBLE);
                recipientListener();}}});

        gentlemanCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ladyChecked = false;
                    recipChosen = true;
                    recipientText.setVisibility(View.VISIBLE);
                    specAreaText.setVisibility(View.VISIBLE);
                    specAreaSwitch.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.VISIBLE);
                    recipientListener();}}});
    }

    public void preContinueButton(View view) {
        Intent intent = new Intent(this, CreateLetterV2.class);
        startActivity(intent);
    }

    public void recipientListener() {
        if (nhsChecked) {
            // NHS Checked
            if (recipChosen) {
                //NHS Checked & Gender Checked
                if (ladyChecked) {
                    //NHS Lady Chosen
                    doctorImg.setVisibility(View.GONE);
                    nurseImg.setVisibility(View.VISIBLE);
                    grandpaImg.setVisibility(View.GONE);
                    grandmaImg.setVisibility(View.GONE);
                    recipientText.setText(R.string.nhs_female_explained);
                } else {
                    //NHS Gentleman Selected
                    doctorImg.setVisibility(View.VISIBLE);
                    nurseImg.setVisibility(View.GONE);
                    grandpaImg.setVisibility(View.GONE);
                    grandmaImg.setVisibility(View.GONE);
                    recipientText.setText(R.string.nhs_male_explained);}}
            else {
                //NHS Checked but no Gender Checked
                doctorImg.setVisibility(View.VISIBLE);
                nurseImg.setVisibility(View.VISIBLE);
                grandpaImg.setVisibility(View.GONE);
                grandmaImg.setVisibility(View.GONE);}
        }
        else {
            //Elderly Checked
            if (recipChosen) {
            //Elderly Checked & Gender Checked
                if (ladyChecked) {
                    //Elderly Lady Chosen
                    doctorImg.setVisibility(View.GONE);
                    nurseImg.setVisibility(View.GONE);
                    grandpaImg.setVisibility(View.GONE);
                    grandmaImg.setVisibility(View.VISIBLE);
                    recipientText.setText(R.string.elderly_female_explained);}
                else {
                    //Elderly Gentleman Selected
                    doctorImg.setVisibility(View.GONE);
                    nurseImg.setVisibility(View.GONE);
                    grandpaImg.setVisibility(View.VISIBLE);
                    grandmaImg.setVisibility(View.GONE);
                    recipientText.setText(R.string.elderly_male_explained);}}
        else {
            //Elderly Checked but no Gender Checked
                doctorImg.setVisibility(View.GONE);
                nurseImg.setVisibility(View.GONE);
                grandpaImg.setVisibility(View.VISIBLE);
                grandmaImg.setVisibility(View.VISIBLE);}}}
}
