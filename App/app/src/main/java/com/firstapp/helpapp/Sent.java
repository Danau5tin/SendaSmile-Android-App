package com.firstapp.helpapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Sent extends AppCompatActivity {

    ImageButton whatsapp;
    String tag;
    PackageManager packageManager;
    final String WHATSAPP_PACKAGE = "com.whatsapp";
    final String FACEBOOK_PACKAGE = "com.facebook.orca";
    final String INSTAGRAM_PACKAGE = "com.instagram.android";
    final String TWITTER_PACKAGE = "com.twitter.android";
    Boolean packageInstalled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_activity);

        whatsapp = findViewById(R.id.whatsappButton);
        packageManager = this.getPackageManager();
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

}
