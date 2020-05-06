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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;


public class PreLetterCreation extends AppCompatActivity {

    Switch specAreaSwitch;
    Spinner destinationSpinner;
    TextView recipientText, genderText, specAreaText, whoToText;
    RadioButton nhsCheck, elderlyCheck, gentlemanCheck, ladyCheck;
    RadioGroup genderRadGroup;
    Boolean recipChosen, regionChosen;
    ImageView nurseImg, doctorImg, grandmaImg, grandpaImg;
    Button continueButton;

    FirebaseAuth firebaseAuth;
    FirebaseHelper fireBaseHelper;
    FirebaseUser firebaseUser;
    DatabaseReference reff;

    public static Boolean nhsChecked;
    public static Boolean ladyChecked;
    public static int sessionNumber;
    String userID;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_letter_creation);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();


        reff = FirebaseDatabase.getInstance().getReference(userID).child("mainUserInfo").child("sessionNum");
        reff.runTransaction(new Transaction.Handler() {
            // Increments the number of sessions user has had.
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                    sessionNumber = 1;
                } else {
                    mutableData.setValue(currentValue + 1);
                    sessionNumber = currentValue+1;
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                System.out.println("Transaction completed");
            }
        });
        fireBaseHelper = new FirebaseHelper(firebaseAuth, FirebaseDatabase.getInstance());

        genderRadGroup = findViewById(R.id.genderRadioGroup);
        genderText = findViewById(R.id.genderText);
        whoToText = findViewById(R.id.whoText);
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
        regionChosen = false;
        recipChosen = false;
        nhsChecked = false;
        ladyChecked = false;

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
                if (isChecked) {destinationSpinner.setVisibility(View.VISIBLE); specAreaSwitch.setText(R.string.Yes);
                regionChosen = true;}
                else {destinationSpinner.setVisibility(View.GONE); specAreaSwitch.setText(R.string.No);
                regionChosen = false;
                    fireBaseHelper.updateCurrentSessionRecipient(nhsChecked, ladyChecked, sessionNumber, "cancelled");}}});


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

        Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.expand_in_small);
        whoToText.startAnimation(expandIn);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Unable to go back to login page!", Toast.LENGTH_SHORT).show();
    }

    public void preContinueButton(View view) {
        if (regionChosen) {
            fireBaseHelper.updateCurrentSessionRecipient(nhsChecked, ladyChecked, sessionNumber, destinationSpinner.getSelectedItem().toString());
        } else {
        fireBaseHelper.updateCurrentSessionRecipient(nhsChecked, ladyChecked, sessionNumber);}
        Intent intent = new Intent(this, CreateLetterV2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
