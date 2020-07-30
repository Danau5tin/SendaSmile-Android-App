package com.firstapp.helpapp;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PreLetterCreation extends AppCompatActivity {

    Switch specAreaSwitch;
    Spinner destinationSpinner;
    TextView recipientText, genderText, specAreaText, whoToText;
    RadioButton keyCheck, elderlyCheck, gentlemanCheck, ladyCheck;
    RadioGroup genderRadGroup;
    Boolean regionChosen;
    ImageView nurseImg, doctorImg, grandmaImg, grandpaImg;
    Button continueButton;

    FirebaseAuth firebaseAuth;
    FirebaseHelper fireBaseHelper;
    FirebaseUser firebaseUser;

    public static Boolean keyWorkerSelected;
    public static Boolean ladyChecked;
    public static int sessionNumber;

    SessionRecipient sessionRecipient;

    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_letter_creation);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        userID = firebaseUser.getUid();

        fireBaseHelper = new FirebaseHelper();
        fireBaseHelper.incrementUserSessions();
        createNewSessionRecipient();

        genderRadGroup = findViewById(R.id.genderRadioGroup);
        genderText = findViewById(R.id.genderText);
        whoToText = findViewById(R.id.whoText);
        keyCheck = findViewById(R.id.keyCheckbox);
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

        setOnCheckedListeners();
        Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.expand_in_small);
        whoToText.startAnimation(expandIn);
    }

    private void createNewSessionRecipient() {
        sessionRecipient = new SessionRecipient();
        sessionRecipient.sessionID = sessionNumber;
    }

    private void setOnCheckedListeners() {
        keyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sessionRecipient.setKeyWorker(true);
                    showCorrectCharacterImages();
                    showGenderOptions();
                    }}});

        elderlyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sessionRecipient.setKeyWorker(false);
                    showCorrectCharacterImages();
                    showGenderOptions();}}});

        ladyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sessionRecipient.setLady(true);
                    showRemainingViews();
                    showCorrectCharacterImages();}}});

        gentlemanCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sessionRecipient.setLady(false);
                    showRemainingViews();
                    showCorrectCharacterImages();}}});

        specAreaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    destinationSpinner.setVisibility(View.VISIBLE); specAreaSwitch.setText(R.string.Yes);
                    regionChosen = true;
                }
                else {
                    destinationSpinner.setVisibility(View.GONE); specAreaSwitch.setText(R.string.No);
                    regionChosen = false;
                    sessionRecipient.setLocality("cancelled");}}});
    }

    private void showGenderOptions() {
        genderRadGroup.setVisibility(View.VISIBLE);
        genderText.setVisibility(View.VISIBLE);
    }

    private void showRemainingViews() {
        recipientText.setVisibility(View.VISIBLE);
        specAreaText.setVisibility(View.VISIBLE);
        specAreaSwitch.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.VISIBLE);
    }

    public void preContinueButton(View view) {
        if (regionChosen) {
            sessionRecipient.setLocality(destinationSpinner.getSelectedItem().toString());
        }
        fireBaseHelper.updateCurrentSessionRecipient(sessionRecipient);
        Intent intent = new Intent(this, CreateLetterV2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showCorrectCharacterImages() {
        if (keyWorkerSelected) {

            if (sessionRecipient.isCompleted()) {
                if (ladyChecked) {
                    showSingleCharacter(nurseImg);
                    recipientText.setText(R.string.key_female_explained);
                } else {
                    showSingleCharacter(doctorImg);
                    recipientText.setText(R.string.key_male_explained);}}
            else {
                showTwoCharacters(nurseImg, doctorImg);}
        }
        else {
            if (sessionRecipient.isCompleted()) {
                if (ladyChecked) {
                    showSingleCharacter(grandmaImg);
                    recipientText.setText(R.string.elderly_female_explained);}
                else {
                    showSingleCharacter(grandpaImg);
                    recipientText.setText(R.string.elderly_male_explained);}}
        else {
            showTwoCharacters(grandmaImg, grandpaImg);}}}

    private void showSingleCharacter(ImageView charImgView) {
        hideAllCharacters();
        charImgView.setVisibility(View.VISIBLE);
    }

    private void hideAllCharacters() {
        doctorImg.setVisibility(View.GONE);
        nurseImg.setVisibility(View.GONE);
        grandpaImg.setVisibility(View.GONE);
        grandmaImg.setVisibility(View.GONE);
    }

    private void showTwoCharacters(ImageView charImgView1, ImageView charImgView2) {
        hideAllCharacters();
        charImgView1.setVisibility(View.VISIBLE);
        charImgView2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Unable to go back to login page!", Toast.LENGTH_SHORT).show();
    }
}
