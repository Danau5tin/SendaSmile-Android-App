package com.firstapp.helpapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;


public class PreLetterCreation extends AppCompatActivity {

    Switch anonSwitch, specAreaSwitch;
    Spinner destinationSpinner;
    TextView recipientText, anonDesc, genderText, genderWarningText, specAreaText, remainAnonText;
    EditText usernameEdit, emailEdit, numEdit;
    RadioButton nhsCheck, elderlyCheck, gentlemanCheck, ladyCheck;
    RadioGroup genderRadGroup;
    Boolean nhsChecked, ladyChecked, recipChosen;
    ImageView personImage;
    InputStream imageStream;
    Button continueButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_letter_creation);

        usernameEdit = findViewById(R.id.userNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        numEdit = findViewById(R.id.numEdit);
        anonDesc = findViewById(R.id.textAnon);
        anonSwitch = findViewById(R.id.remainAnonSwitch);
        genderRadGroup = findViewById(R.id.genderRadioGroup);
        genderText = findViewById(R.id.genderText);
        nhsCheck = findViewById(R.id.nhsCheckbox);
        elderlyCheck = findViewById(R.id.elderlyCheckbox);
        gentlemanCheck = findViewById(R.id.gentlemanCheckbox);
        ladyCheck = findViewById(R.id.ladyCheckbox);
        recipientText = findViewById(R.id.textRecipientDescr);
        specAreaText = findViewById(R.id.specAreaText);
        remainAnonText = findViewById(R.id.remainAnonText);
        specAreaSwitch = findViewById(R.id.specAreaSwitch);
        personImage = findViewById(R.id.personImgView);
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
                    if (recipChosen){
                recipientListener();} else {
                        genderRadGroup.setVisibility(View.VISIBLE);
                        genderText.setVisibility(View.VISIBLE);
                    }}}});

        elderlyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nhsChecked = false;
                    ladyChecked = true;
                    if (recipChosen){
                recipientListener();} else {
                        genderRadGroup.setVisibility(View.VISIBLE);
                        genderText.setVisibility(View.VISIBLE);
                        recipientListener();}}}});

        ladyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ladyChecked = true;
                    recipChosen = true;
                    recipientText.setVisibility(View.VISIBLE);
                    remainAnonText.setVisibility(View.VISIBLE);
                    anonSwitch.setVisibility(View.VISIBLE);
                    specAreaText.setVisibility(View.VISIBLE);
                    specAreaSwitch.setVisibility(View.VISIBLE);
                    anonDesc.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.VISIBLE);
                recipientListener();}}});

        gentlemanCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ladyChecked = false;
                    recipChosen = true;
                    recipientText.setVisibility(View.VISIBLE);
                    remainAnonText.setVisibility(View.VISIBLE);
                    anonSwitch.setVisibility(View.VISIBLE);
                    specAreaText.setVisibility(View.VISIBLE);
                    specAreaSwitch.setVisibility(View.VISIBLE);
                    anonDesc.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.VISIBLE);
                    recipientListener();}}});

        anonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hide UI elements for user info
                    usernameEdit.setVisibility(View.VISIBLE);
                    emailEdit.setVisibility(View.VISIBLE);
                    numEdit.setVisibility(View.VISIBLE);
                    anonSwitch.setText(R.string.No);
                    anonDesc.setText(R.string.non_anon_expl);
                } else {
                    // Show UI elements for user info
                    usernameEdit.setVisibility(View.GONE);
                    emailEdit.setVisibility(View.GONE);
                    numEdit.setVisibility(View.GONE);
                    anonSwitch.setText(R.string.Yes);
                    anonDesc.setText(R.string.anon_expl);
                    }}});
    }

    public void preContinueButton(View view) {
        Intent intent = new Intent(this, CreateLetter.class);
        startActivity(intent);
    }

    public void recipientListener() {
        if (nhsChecked) {
            if (ladyChecked) {recipientText.setText(R.string.nhs_female_explained);
            imageStream = this.getResources().openRawResource(R.raw.nurse);}
            else {recipientText.setText(R.string.nhs_male_explained);
                imageStream = this.getResources().openRawResource(R.raw.doctor);}
        } else {
            if (ladyChecked) {recipientText.setText(R.string.elderly_female_explained);
                imageStream = this.getResources().openRawResource(R.raw.grandma);}
            else {
            recipientText.setText(R.string.elderly_male_explained);
                imageStream = this.getResources().openRawResource(R.raw.grandpa);}}
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        personImage.setImageBitmap(bitmap);
    }
}
