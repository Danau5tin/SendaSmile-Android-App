package com.firstapp.helpapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import com.firebase.ui.auth.IdpResponse;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "Delivery";
    List<AuthUI.IdpConfig> providers;
    final int AUTH_CODE = 30;
    NotificationCompat.Builder builder;
    TextView sloganText;
    LinearLayout one, two, three;
    Button homeBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sloganText = findViewById(R.id.sloganText);
        Animation bounceIn = AnimationUtils.loadAnimation(this, R.anim.bounce_in);
        one = findViewById(R.id.stepOneLayout);
        two = findViewById(R.id.stepTwoLayout);
        three = findViewById(R.id.stepThreeLayout);
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation slideDownTwo = AnimationUtils.loadAnimation(this, R.anim.slide_down_two);
        Animation slideDownThree = AnimationUtils.loadAnimation(this, R.anim.slide_down_three);
        homeBut = findViewById(R.id.homeButtonStart);
        Animation expandDelayed = AnimationUtils.loadAnimation(this, R.anim.expand_delayed_main);

        sloganText.startAnimation(bounceIn);
        one.startAnimation(slideDown);
        two.startAnimation(slideDownTwo);
        three.startAnimation(slideDownThree);
        homeBut.startAnimation(expandDelayed);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startAlarm(NotificationHelper.REMINDER_NOT_TIME);

    }

    @SuppressLint("ResourceType")
    public void homeStartButton(View view) {
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
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast.makeText(this, "Successful Sign In", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, PreLetterCreation.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "NOT SUCCESSFUL Sign In", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
