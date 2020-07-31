package com.firstapp.helpapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.helpapp.helper.FeedbackPackage;
import com.firstapp.helpapp.helper.FirebaseHelper;

public class SentActivity extends AppCompatActivity {

    ImageButton whatsapp;
    String tag;
    ImageButton thumbsUp, thumbsDown;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_activity);

        whatsapp = findViewById(R.id.whatsappButton);
        thumbsUp = findViewById(R.id.thumbsUp);
        thumbsDown = findViewById(R.id.thumbsDown);
        firebaseHelper = new FirebaseHelper();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your Letter has been sent already!", Toast.LENGTH_SHORT).show();
        }

    public void sentBackHome(View view) {
        Intent intent = new Intent(this, IntroHomeActivity.class);
        startActivity(intent);
    }

    public void shareButtonPressed(View view) {
        FeedbackPackage feedbackPackage = new FeedbackPackage(this, firebaseHelper);
        tag = String.valueOf(view.getTag());
        feedbackPackage.launchExternalApp(tag);
    }

    public void feedbackButtonPressed(View view) {
        String tag = view.getTag().toString();
        firebaseHelper.updateBasicFeedbackButton(tag);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Any further feedback?");

        final EditText input = new EditText(this);
        input.setHint("A line or two from you...");
        builder.setView(input);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String feedbackString = input.getText().toString();
                firebaseHelper.updateFeedbackString(feedbackString);
                showFeedbackThanksToast();
            }
        });
        builder.setNegativeButton("No thank you", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showFeedbackThanksToast();
            }
        });
        builder.show();

    }

    public void showFeedbackThanksToast() {
        Toast.makeText(getBaseContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
    }
}
