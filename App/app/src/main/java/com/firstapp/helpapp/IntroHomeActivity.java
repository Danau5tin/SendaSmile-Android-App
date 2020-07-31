package com.firstapp.helpapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firstapp.helpapp.helper.AlertReciever;
import com.firstapp.helpapp.helper.NotificationHelper;


import java.util.Arrays;
import java.util.List;

public class IntroHomeActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
    final int AUTH_CODE = 30;
    TextView sloganText;
    LinearLayout stepOnelayout, stepTwoLayout, stepThreeLayout;
    Button homeBut;
    Animation bounceIn, slideDown, slideDownTwo, slideDownThree, expandDelayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_home_activity);
        sloganText = findViewById(R.id.sloganText);
        stepOnelayout = findViewById(R.id.stepOneLayout);
        stepTwoLayout = findViewById(R.id.stepTwoLayout);
        stepThreeLayout = findViewById(R.id.stepThreeLayout);
        homeBut = findViewById(R.id.homeButtonStart);

        setAnimations();
        runAnimations();

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startAlarm(NotificationHelper.REMINDER_NOT_TIME);
    }

    private void setAnimations() {
        bounceIn = AnimationUtils.loadAnimation(this, R.anim.bounce_in);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        slideDownTwo = AnimationUtils.loadAnimation(this, R.anim.slide_down_two);
        slideDownThree = AnimationUtils.loadAnimation(this, R.anim.slide_down_three);
        expandDelayed = AnimationUtils.loadAnimation(this, R.anim.expand_delayed_main);
    }

    private void runAnimations() {
        sloganText.startAnimation(bounceIn);
        stepOnelayout.startAnimation(slideDown);
        stepTwoLayout.startAnimation(slideDownTwo);
        stepThreeLayout.startAnimation(slideDownThree);
        homeBut.startAnimation(expandDelayed);
    }

    @SuppressLint("ResourceType")
    public void homeStartButtonPressed(View view) {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.CustomTheme)
                .setLogo(R.raw.rainbow_logo)
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://dan96austin.wixsite.com/mysite/terms-of-service",
                                "https://dan96austin.wixsite.com/mysite/copy-of-terms-of-service")
                .build(),
                AUTH_CODE);
    }

    private void startAlarm(int secs) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        intent.putExtra("notification", NotificationHelper.REMINDER_NOT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        long milliSecsFromNow = System.currentTimeMillis() + secs * 1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, milliSecsFromNow, pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successful Sign In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PreLetterCreationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                Toast.makeText(this, "NOT SUCCESSFUL Sign In", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
