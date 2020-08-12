package com.firstapp.helpapp.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.firstapp.helpapp.R;

public class FeedbackPackage {

    final String WHATSAPP_PACKAGE = "com.whatsapp";
    final String FACEBOOK_PACKAGE = "com.facebook.orca";
    final String INSTAGRAM_PACKAGE = "com.instagram.android";
    final String TWITTER_PACKAGE = "com.twitter.android";


    private PackageManager packageManager;
    private Context context;
    private FirebaseHelper firebaseHelper;

    public FeedbackPackage(Context context, FirebaseHelper firebaseHelper) {
        this.context = context;
        this.packageManager = context.getPackageManager();
        this.firebaseHelper = firebaseHelper;
    }

    public void launchExternalApp (String packageName) {
        firebaseHelper.updateShareButtons(packageName);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.referral));
        sendIntent.setType("text/plain");
        boolean packageInstalled = isPackageInstalled(packageName);

        if (packageInstalled) {
            switch (packageName) {
                case WHATSAPP_PACKAGE:
                    sendIntent.setPackage(WHATSAPP_PACKAGE);
                    context.startActivity(sendIntent);
                    break;
                case INSTAGRAM_PACKAGE:
                    sendIntent.setPackage(INSTAGRAM_PACKAGE);
                    context.startActivity(sendIntent);
                    break;
                case TWITTER_PACKAGE:
                    sendIntent.setPackage(TWITTER_PACKAGE);
                    context.startActivity(sendIntent);
                    break;
                case FACEBOOK_PACKAGE:
                    sendIntent.setPackage(FACEBOOK_PACKAGE);
                    context.startActivity(sendIntent);
                    break;
            }
        } else {
            String appName = "";
            switch (packageName) {
                case WHATSAPP_PACKAGE:
                    appName = "WhatsApp";
                    break;
                case FACEBOOK_PACKAGE:
                    appName = "Facebook Messenger";
                    break;
                case TWITTER_PACKAGE:
                    appName = "Twitter";
                    break;
                case INSTAGRAM_PACKAGE:
                    appName = "Instagram";
                    break;
            }
            Toast.makeText(context, "Could not locate " + appName + ", try one of these instead!", Toast.LENGTH_LONG).show();
        }
        context.startActivity(sendIntent);

    }

    private boolean isPackageInstalled(String packageName) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
