package com.firstapp.helpapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CreateLetter extends AppCompatActivity {

    Button addImageBut, continueBut;
    ImageView userImgView;
    EditText mainUserLetter;
    Boolean userhasInput, userHasImage;
    final int IMAGE_PICK_CODE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_letter_activity);

        userImgView = findViewById(R.id.userImageView);
        addImageBut = findViewById(R.id.addImageBut);
        continueBut = findViewById(R.id.createContinueBut);
        mainUserLetter = findViewById(R.id.mainLetterText);

        continueBut.setEnabled(false);
    }

    public void createContinueButton(View view) {
        if (mainUserLetter.getText().toString().length() > 7) {
        Intent intent = new Intent(this, ConfirmSend.class);
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
            userImgView.setImageURI(data.getData());
            addImageBut.setText(R.string.change_image);
            Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            continueBut.setEnabled(true);
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

}