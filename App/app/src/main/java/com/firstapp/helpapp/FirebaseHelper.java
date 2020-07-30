package com.firstapp.helpapp;


import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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

    private DatabaseReference currentPath;
    private String currentRecipientPath;
    private String currentLetterDetailsPath;

    FirebaseHelper(){
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = firebaseAuth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();
        this.userID = firebaseUser.getUid();
    }

    public void incrementUserSessions() {
        currentPath = database.getReference(userID).child("mainUserInfo").child("sessionNum");

        currentPath.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                    PreLetterCreation.sessionNumber = 1;
                } else {
                    mutableData.setValue(currentValue + 1);
                    PreLetterCreation.sessionNumber = currentValue + 1;
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                System.out.println("Transaction completed");
            }
        });
    }

    public void updateShareButtons(String tag) {
        String shareDetails = "/shareDetails";
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

    public void updateCurrentSessionRecipient(SessionRecipient sessionRecipient){
        currentRecipientPath = userID + currentSession + "/" + sessionRecipient.sessionID + recipData;
        currentPath.setValue(sessionRecipient);
    }

    public void updateLetterDetails(String mainText) {
        currentLetterDetailsPath = userID + currentSession + "/" + sessionID + letterDetails;
        String mainTextPath = "/mainText";
        currentPath = database.getReference(currentLetterDetailsPath + mainTextPath);
        currentPath.setValue(mainText);
    }

    public void updateDeliveryMethodDetails(Boolean paid) {
        String deliveryDetails = "/deliveryDetailsPaid";
        String currentLetterDeliveryPath = userID + currentSession + "/" + sessionID + deliveryDetails;
        currentPath = database.getReference(currentLetterDeliveryPath);
        currentPath.setValue(paid);
    }

}
