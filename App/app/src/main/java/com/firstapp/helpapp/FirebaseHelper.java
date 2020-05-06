package com.firstapp.helpapp;


import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseHelper {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    int sessionID = PreLetterCreation.sessionNumber;
    String userID;
    private String currentSession = "/currentsession";
    private String recipData = "/recipientdata";
    private String letterDetails = "/letterDetails";
    private String feedbackDetails = "/feedbackDetails";
    private String shareDetails = "/shareDetails";
    private String deliveryDetails = "/deliveryDetailsPaid";


    //Paths
    private DatabaseReference currentPath;
    private String currentRecipientPath;
    private String currentLetterDetailsPath;
    private String currentLetterDeliveryPath;

    FirebaseHelper(FirebaseAuth auth, FirebaseDatabase database){
        this.firebaseAuth = auth.getInstance();
        this.firebaseUser = firebaseAuth.getCurrentUser();
        this.database = database.getInstance();
        this.userID = firebaseUser.getUid();
    }

    public void updateShareButtons(String tag) {
        String currentSharePath = userID + currentSession + "/" + sessionID + shareDetails;
        String shareButtonClickedPath = "/" + tag;
        currentPath = database.getReference(currentSharePath + shareButtonClickedPath);
        currentPath.setValue("clicked");
    }

    public void updateBasicFeedbackButton(String tag) {
        String currentFeedbackPath = userID + currentSession + "/" + sessionID + feedbackDetails;
        String basicFeedback = "/basicThumbsFeedback";
        currentPath = database.getReference(currentFeedbackPath + basicFeedback);
        currentPath.setValue(tag);
    }

    public void updateFeedbackString(String feedbackText) {
        String currentFeedbackPath = userID + currentSession + "/" + sessionID + feedbackDetails;
        String feedbackStringPath = "/furtherDetail";
        currentPath = database.getReference(currentFeedbackPath + feedbackStringPath);
        currentPath.setValue(feedbackText);
    }

    public void uploadImage(Uri imageRef){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference userStorageRef = storageReference.child(userID + "/" + sessionID);
        UploadTask uploadTask = userStorageRef.putFile(imageRef);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                updateImageUrl(downloadUrl);
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                CreateLetterV2.imageUploadProgress = taskSnapshot.getBytesTransferred();
            }
        });
    }

    public void updateImageUrl(String url) {
        currentLetterDetailsPath = userID + currentSession + "/" + sessionID + letterDetails;

        String imageUrlPath = "/imageURL";
        currentPath = database.getReference(currentLetterDetailsPath + imageUrlPath);
        currentPath.setValue(url);
    }


    public void updateCurrentSessionRecipient(Boolean nhs, Boolean lady, int sessionID){
        // Update recipient type
        currentRecipientPath = userID + currentSession + "/" + sessionID + recipData;

        String typePath = "/type";
        String genderPath = "/gender";
        currentPath = database.getReference(currentRecipientPath + typePath);
        if (nhs) {currentPath.setValue("nhs");} else {currentPath.setValue("elderly");}
        currentPath = database.getReference(currentRecipientPath + genderPath);
        if (lady) {currentPath.setValue("lady");} else {currentPath.setValue("gentleman");}
    }

    public void updateCurrentSessionRecipient(Boolean nhs, Boolean lady, int sessionId, String local){
        // Update current recipient type with locality
        currentRecipientPath = userID + currentSession + "/" + sessionId + recipData;
        String typePath = "/type";
        String genderPath = "/gender";
        String localityPath = "/locality";
        currentPath = database.getReference(currentRecipientPath + typePath);
        if (nhs) {currentPath.setValue("nhs");} else {currentPath.setValue("elderly");}
        currentPath = database.getReference(currentRecipientPath + genderPath);
        if (lady) {currentPath.setValue("lady");} else {currentPath.setValue("gentleman");}
        currentPath.setValue("gentleman");
        currentPath = database.getReference(currentRecipientPath + localityPath);
        currentPath.setValue(local);
    }

    public void updateLetterDetails(String mainText) {
        // Update current letter details
        currentLetterDetailsPath = userID + currentSession + "/" + sessionID + letterDetails;

        String mainTextPath = "/mainText";
        currentPath = database.getReference(currentLetterDetailsPath + mainTextPath);
        currentPath.setValue(mainText);
    }

    public void updateDeliveryMethodDetails(Boolean paid) {
        // Update current delivery choice
        currentLetterDeliveryPath = userID + currentSession + "/" + sessionID + deliveryDetails;
        currentPath = database.getReference(currentLetterDeliveryPath);
        currentPath.setValue(paid);
    }

}
