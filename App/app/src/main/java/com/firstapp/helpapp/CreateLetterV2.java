package com.firstapp.helpapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CreateLetterV2 extends AppCompatActivity {

    Button continueBut, changeImage, deliveryOption;
    String result;
    Switch anonSwitch;
    TextView anonDescr;
    Spinner greetingSpinner;
    EditText mainUserLetter, nameEdit, numEdit, emailEdit;
    ImageButton addImageBut;
    ImageView userImgView;
    final int IMAGE_PICK_CODE = 10;
    final int DELIVERY_OPTION_CODE = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_letter_activity_v2);

        userImgView = findViewById(R.id.userImageViewV2);
        addImageBut = findViewById(R.id.addimageButV2);
        continueBut = findViewById(R.id.createContinueButV2);
        mainUserLetter = findViewById(R.id.mainLetterTextV2);
        changeImage = findViewById(R.id.changeImageV2);
        changeImage.setVisibility(View.GONE);
        deliveryOption = findViewById(R.id.deliveryOptionBut);
        greetingSpinner = findViewById(R.id.greetingSpinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.male_greetings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        greetingSpinner.setAdapter(adapter);

        continueBut.setEnabled(false);
        changeImage.setVisibility(View.VISIBLE);
        changeImage.setText("Add Image");
    }

    public void createContinueButton(View view) {
        if (mainUserLetter.getText().toString().length() > 7) {
        Intent intent = new Intent(this, Sent.class);
        startActivity(intent);} else {
            Toast.makeText(this, "Letter must contain more than 20 words", Toast.LENGTH_LONG).show();
        }
    }


    public void addImageButton(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    IMAGE_PICK_CODE);
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_PICK_CODE) {
            userImgView.setImageURI(data.getData());
            userImgView.setVisibility(View.VISIBLE);
            addImageBut.setVisibility(View.GONE);
            changeImage.setVisibility(View.VISIBLE);
            changeImage.setText("Change Image");
            Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            continueBut.setEnabled(true);}
            else if (requestCode == DELIVERY_OPTION_CODE) {
                 result = data.getStringExtra("result");
                if (result.equals("paid")){
                    deliveryOption.setText(getString(R.string.paid_chosen));
                } else {
                    deliveryOption.setText(getString(R.string.free_chosen));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case IMAGE_PICK_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    // permission denied.
                    Toast.makeText(this, "Permission required to add a photo!", Toast.LENGTH_LONG).show();
                }
                return;
            }
                }
            }

    public void deliveryButtonPressed(View view) {
        Intent intent = new Intent(this, ConfirmSend.class);
        startActivityForResult(intent, DELIVERY_OPTION_CODE);
    }
}