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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateLetterV2 extends AppCompatActivity {

    Button continueBut, changeImage, deliveryOption;
    TextView writingToText;
    Boolean result, deliveryChosen, imageRequired, imageChosen;
    EditText mainUserLetter;
    LinearLayout deliveryLayout;
    ConstraintLayout imageLayout;
    ImageButton addImageBut;
    ImageView userImgView;
    final int IMAGE_PICK_CODE = 10;
    final int DELIVERY_OPTION_CODE = 20;
    FirebaseAuth firebaseAuth;
    FirebaseHelper fireBaseHelper;
    Animation expandIn;
    RadioButton yesPhoto, noPhoto;
    Uri uri;
    public static long imageUploadProgress;

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
        writingToText = findViewById(R.id.writingToText);
        deliveryLayout = findViewById(R.id.deliveryLayout);
        imageLayout = findViewById(R.id.imageLayout);
        yesPhoto = findViewById(R.id.yesPhotoRadio);
        noPhoto = findViewById(R.id.noPhotoRadio);
        deliveryChosen = false;
        imageChosen = false;
        imageRequired =  true;
        firebaseAuth = FirebaseAuth.getInstance();
        fireBaseHelper = new FirebaseHelper(firebaseAuth, FirebaseDatabase.getInstance());



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
                    imageRequired = true;
                }
            }
        });
        noPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageLayout.setVisibility(View.GONE);
                    imageRequired = false;
                    deliveryLayout.setVisibility(View.VISIBLE);
                    fireBaseHelper.updateImageUrl("Image not chosen or possibly uploaded but cancelled");
                }
            }
        });
    }

    public void writeToListener() throws ParseException {
        Boolean ladyChecked = PreLetterCreation.ladyChecked;
        Boolean nhsChecked = PreLetterCreation.nhsChecked;

        if (nhsChecked) {
            if (ladyChecked) {//Lady NHS Chosen
                writingToText.setText(getString(R.string.rec_conf_nhs_lady));
                 } else {
                //Gentleman NHS Chosen
                writingToText.setText(getString(R.string.rec_conf_nhs_gentleman));
            }
        } else {

            String lockDownDate= "23/03/2020";
            Date currentTime = Calendar.getInstance().getTime();
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
            String FinalDate = dates.format(currentTime);
            date1 = dates.parse(lockDownDate);
            date2 = dates.parse(FinalDate);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);

            if (ladyChecked) {
                //Elderly Lady Chosen
                String elderlyLady = getString(R.string.rec_conf_elderly_lady_start) + " " + dayDifference + " " + getString(R.string.rec_con_elderly_end);
                writingToText.setText(elderlyLady);
            } else {
                //Elderly Gentleman Chosen
                String elderlyGentleman = getString(R.string.rec_conf_elderly_gentleman_start) + " " + dayDifference + " " + getString(R.string.rec_con_elderly_end);
                writingToText.setText(elderlyGentleman);
            }
        }
    }

    public void createContinueButton(View view) {
        // User finished with activity
        fireBaseHelper.updateLetterDetails(mainUserLetter.getText().toString());


        if (mainUserLetter.getText().toString().length() > 7) {
            // Letter has text.
            if (!imageRequired) {
                //Letter has text and image not required
                if (deliveryChosen) {
                startAlarm(NotificationHelper.DELIVERY_NOT_TIME);
                Intent intent = new Intent(this, Sent.class);
                startActivity(intent);}
                else {Toast.makeText(this, "Please choose a delivery method", Toast.LENGTH_LONG).show();
                    deliveryLayout.startAnimation(expandIn);}
            } else {
                //Letter has text and image is required
                if (imageChosen) {
                    if (deliveryChosen) {

                        if (imageUploadProgress >= 95) {
                        startAlarm(NotificationHelper.DELIVERY_NOT_TIME);
                        Intent intent = new Intent(this, Sent.class);
                        startActivity(intent);}
                        else {
                            Toast.makeText(this, "Please wait, uploading image: " + imageUploadProgress, Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {Toast.makeText(this, "Please choose a delivery method", Toast.LENGTH_LONG).show();
                        deliveryLayout.startAnimation(expandIn);}
                }
                else {
                    // image required but not selected
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_LONG).show();
                changeImage.startAnimation(expandIn);}
            }
        } else {
            // Letter has no text
            Toast.makeText(this, "Letter must contain more than 20 words", Toast.LENGTH_LONG).show();
        }
    }

    private void startAlarm(int secs) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        intent.putExtra("notification", NotificationHelper.DELIVERY_NOT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        long milliSecsFromNow = System.currentTimeMillis() + secs * 1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, milliSecsFromNow, pendingIntent);
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

        fireBaseHelper.updateLetterDetails(mainUserLetter.getText().toString());

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
            imageChosen = true;}}
            else if (requestCode == DELIVERY_OPTION_CODE) {
                 result = data.getBooleanExtra("result", false);
                 deliveryChosen = true;
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