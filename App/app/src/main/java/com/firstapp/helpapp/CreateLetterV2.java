package com.firstapp.helpapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;

import static com.firstapp.helpapp.MainLetterDetails.*;
import static com.firstapp.helpapp.PreLetterCreation.*;

public class CreateLetterV2 extends AppCompatActivity {

    Button continueBut, changeImage, deliveryOption;
    TextView writingToText;
    MainLetterDetails mainLetterDetails;
    EditText mainUserLetter;
    LinearLayout deliveryLayout;
    ConstraintLayout imageLayout;
    ImageButton addImageBut;
    ImageView userImgView;
    final int IMAGE_PICK_CODE = 10;
    final int DELIVERY_OPTION_CODE = 20;
    FirebaseHelper fireBaseHelper;
    Animation expandIn;
    RadioButton yesPhoto, noPhoto;
    Uri uri;
    public static long imageUploadProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_letter_activity_v2);
        mainLetterDetails = new MainLetterDetails();

        userImgView = findViewById(R.id.userImageViewV2);
        addImageBut = findViewById(R.id.addimageButV2);
        continueBut = findViewById(R.id.createContinueButV2);
        mainUserLetter = findViewById(R.id.mainLetterTextV2);
        changeImage = findViewById(R.id.changeImageV2);
        changeImage.setVisibility(View.GONE);
        deliveryOption = findViewById(R.id.deliveryOptionBut);
        writingToText = findViewById(R.id.writingToText);
        deliveryLayout = findViewById(R.id.deliveryLayout);
        imageLayout = findViewById(R.id.imageLayout);
        yesPhoto = findViewById(R.id.yesPhotoRadio);
        noPhoto = findViewById(R.id.noPhotoRadio);

        fireBaseHelper = new FirebaseHelper();

        changeImage.setVisibility(View.VISIBLE);
        changeImage.setText(R.string.add_image);
        deliveryLayout.setVisibility(View.INVISIBLE);

        try {
            writeToListener();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        expandIn = AnimationUtils.loadAnimation(this, R.anim.expand_in);
        writingToText.startAnimation(expandIn);

        yesPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageLayout.setVisibility(View.VISIBLE);
                    mainLetterDetails.setImageRequired(true);
                }
            }
        });
        noPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageLayout.setVisibility(View.GONE);
                    deliveryLayout.setVisibility(View.VISIBLE);
                    mainLetterDetails.setImageRequired(false);
                }
            }
        });
    }

    public void writeToListener() throws ParseException {
        Boolean ladyChecked = sessionRecipient.lady;
        Boolean keyWorkerChecked = sessionRecipient.keyWorker;

        if (keyWorkerChecked) {
            if (ladyChecked) {
                writingToText.setText(getString(R.string.rec_conf_key_lady));
                 } else {
                writingToText.setText(getString(R.string.rec_conf_key_gentleman));
            }
        } else {
            String lockDownDate= "23/03/2020";
            Long differenceDates = new DateHelper().numDaysBetweenNowDate(lockDownDate);
            String dayDifference = Long.toString(differenceDates);
            if (ladyChecked) {
                String elderlyLady = getString(R.string.rec_conf_elderly_lady_start) +
                        " " + dayDifference + " " + getString(R.string.rec_con_elderly_end);
                writingToText.setText(elderlyLady);
            } else {
                String elderlyGentleman = getString(R.string.rec_conf_elderly_gentleman_start) +
                        " " + dayDifference + " " + getString(R.string.rec_con_elderly_end);
                writingToText.setText(elderlyGentleman);
            }
        }
    }

    public void createContinueButton(View view) {
        mainLetterDetails.setMainText(mainUserLetter.getText().toString());
        LETTER_RESULT result = mainLetterDetails.isOkayToUpload();
        switch (result) {
            case SMALL_TEXT: Toast.makeText(this, "Letter must contain more than " +
                    "30 characters", Toast.LENGTH_LONG).show();
            case NO_IMAGE: {
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_LONG).show();
                changeImage.startAnimation(expandIn);
            }
            case NO_DELIVERY: {
                Toast.makeText(this, "Please choose a delivery method", Toast.LENGTH_LONG).show();
                deliveryLayout.startAnimation(expandIn);
            }
            case UPLOADING_IMG: Toast.makeText(this, "Please wait, uploading image: "
                            + imageUploadProgress + "%",
                    Toast.LENGTH_SHORT).show();
            case SUCCESS: {
                fireBaseHelper.updateLetterDetails(mainLetterDetails);
                startAlarm();
                Intent intent = new Intent(this, Sent.class);
                startActivity(intent);
            }
        }
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        intent.putExtra("notification", NotificationHelper.DELIVERY_NOT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        long milliSecsFromNow = System.currentTimeMillis() + NotificationHelper.DELIVERY_NOT_TIME * 1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, milliSecsFromNow, pendingIntent);
    }

    public void addImageButton(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
                if (data == null) {Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();}
                else {
                    uri= data.getData();
                    fireBaseHelper.uploadImage(uri);
                    userImgView.setImageURI(uri);
                    userImgView.setVisibility(View.VISIBLE);
                    addImageBut.setVisibility(View.GONE);
                    changeImage.setVisibility(View.VISIBLE);
                    changeImage.setText(getString(R.string.change_image));
                    deliveryLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
                    mainLetterDetails.setImageChosen(true);}}
            else if (requestCode == DELIVERY_OPTION_CODE) {
                assert data != null;
                boolean result = data.getBooleanExtra("result", false);
                mainLetterDetails.setDeliveryChosen(true);
                if (result){
                    deliveryOption.setText(getString(R.string.paid_chosen));
                    fireBaseHelper.updateDeliveryMethodDetails(true);
                } else {
                    deliveryOption.setText(getString(R.string.free_chosen));
                    fireBaseHelper.updateDeliveryMethodDetails(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == IMAGE_PICK_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission required to add a photo!", Toast.LENGTH_LONG).show();
            }
        }
            }

    public void deliveryButtonPressed(View view) {
        Intent intent = new Intent(this, ConfirmSend.class);
        startActivityForResult(intent, DELIVERY_OPTION_CODE);
    }
}