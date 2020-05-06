package com.firstapp.helpapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Sent extends AppCompatActivity {

    ImageButton whatsapp;
    String tag;
    PackageManager packageManager;
    final String WHATSAPP_PACKAGE = "com.whatsapp";
    final String FACEBOOK_PACKAGE = "com.facebook.orca";
    final String INSTAGRAM_PACKAGE = "com.instagram.android";
    final String TWITTER_PACKAGE = "com.twitter.android";
    Boolean packageInstalled = false;
    ImageButton thumbsUp, thumbsDown;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_activity);

        whatsapp = findViewById(R.id.whatsappButton);
        thumbsUp = findViewById(R.id.thumbsUp);
        thumbsDown = findViewById(R.id.thumbsDown);
        packageManager = this.getPackageManager();
        firebaseHelper = new FirebaseHelper(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance());
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your Letter has been sent already!", Toast.LENGTH_SHORT).show();
        }

    public void sentBackHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void shareButtonPressed(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.referral));
        sendIntent.setType("text/plain");
        tag = String.valueOf(view.getTag());
        packageInstalled = isPackageInstalled(tag, packageManager);
        firebaseHelper.updateShareButtons(tag);

        if (packageInstalled) {
            if (tag.equals(WHATSAPP_PACKAGE)) {sendIntent.setPackage(WHATSAPP_PACKAGE); startActivity(sendIntent);}
            else if (tag.equals(INSTAGRAM_PACKAGE)) {sendIntent.setPackage(INSTAGRAM_PACKAGE); startActivity(sendIntent);}
            else if (tag.equals(TWITTER_PACKAGE)) {sendIntent.setPackage(TWITTER_PACKAGE); startActivity(sendIntent);}
            else if (tag.equals(FACEBOOK_PACKAGE)) {sendIntent.setPackage(FACEBOOK_PACKAGE); startActivity(sendIntent);}
        } else {
            String appName = "";
            if (tag.equals(WHATSAPP_PACKAGE)) {appName = "WhatsApp";}
            else if (tag.equals(FACEBOOK_PACKAGE)) {appName = "Facebook Messenger";}
            else if (tag.equals(TWITTER_PACKAGE)) {appName = "Twitter";}
            if (tag.equals(INSTAGRAM_PACKAGE)) {appName = "Instagram";}
            Toast.makeText(this, "Could not locate " + appName + ", try one of these instead!", Toast.LENGTH_LONG).show();
        }
        startActivity(sendIntent);
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
