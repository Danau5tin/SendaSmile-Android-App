package com.firstapp.helpapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.firstapp.helpapp.PreLetterCreation.*;

public class ConfirmSend extends AppCompatActivity implements PurchasesUpdatedListener {

    RadioButton freeRadio, paidRadio;
    ImageView characterImg;
    TextView deliveryText;
    private BillingClient billingClient;
    private ArrayList<String> skuList = new ArrayList<>();

    private String sku = "new_letter_3_gbp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_send_activity);

        freeRadio = findViewById(R.id.freeRadioBut);
        paidRadio = findViewById(R.id.paidRadioBut);
        paidRadio.setEnabled(false);
        deliveryText = findViewById(R.id.deliveryExplText);
        characterImg = findViewById(R.id.characterIMG);
        skuList.add(sku);
        setUpBillingClient();

        freeRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",false);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        setCharacterImg();
    }

    private void setUpBillingClient() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    loadAllSkus();
                }
            }
            @Override
            public void onBillingServiceDisconnected() { }
        });
    }

    private void loadAllSkus() {
        if (billingClient.isReady()) {

            final SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(final BillingResult billingResult, List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Object skuDetailsObject : list) {
                            final SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                            if (skuDetails.getSku().equals(sku)) {
                                paidRadio.setEnabled(true);
                                paidRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        BillingFlowParams billingFlowParams = BillingFlowParams
                                                .newBuilder()
                                                .setSkuDetails(skuDetails)
                                                .build();
                                        billingClient.launchBillingFlow(ConfirmSend.this, billingFlowParams);
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        int responseCode = billingResult.getResponseCode();

        if (responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }

        } else {
            Toast.makeText(this, "Unsuccessful",Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getSku().equals(sku)){

            if (!purchase.isAcknowledged()) {
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

                ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(BillingResult billingResult, String s) {
                        Toast.makeText(ConfirmSend.this, "Success! Thank you", Toast.LENGTH_SHORT).show();
                    }
                };
                billingClient.consumeAsync(consumeParams, consumeResponseListener);
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please choose option", Toast.LENGTH_SHORT).show();
    }

    public void setCharacterImg(){
        Bitmap bitmap;
        InputStream imageStream;

        if (sessionRecipient.keyWorker) {
            deliveryText.setText(getString(R.string.options_desc_key));
            if (sessionRecipient.lady) {
                imageStream = this.getResources().openRawResource(R.raw.nurse);
            } else {
                imageStream = this.getResources().openRawResource(R.raw.doctor);
            }
        } else {
            deliveryText.setText(getString(R.string.options_desc_elderly));
            if (sessionRecipient.lady) {
                imageStream = this.getResources().openRawResource(R.raw.grandma);
            } else {
                imageStream = this.getResources().openRawResource(R.raw.grandpa);
            }
        }
        bitmap = BitmapFactory.decodeStream(imageStream);
        characterImg.setImageBitmap(bitmap);
    }

}
