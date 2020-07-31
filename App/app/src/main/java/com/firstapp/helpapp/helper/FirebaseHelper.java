package com.firstapp.helpapp.helper;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firstapp.helpapp.CreateLetterActivity;
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

import static com.firstapp.helpapp.PreLetterCreationActivity.*;

public class FirebaseHelper {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    String userID;
    private String currentSession = "/currentSession";
    private String feedbackDetails = "/feedbackDetails";

    private DatabaseReference currentPath;

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
                    sessionRecipient.sessionID = 1;
                } else {
                    mutableData.setValue(currentValue + 1);
                    sessionRecipient.sessionID = currentValue + 1;
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
        String currentSharePath = userID + currentSession + "/" + sessionRecipient.sessionID + shareDetails;
        String shareButtonClickedPath = "/" + tag;
        currentPath = database.getReference(currentSharePath + shareButtonClickedPath);
        currentPath.setValue("clicked");
    }

    public void updateBasicFeedbackButton(String tag) {
        String currentFeedbackPath = userID + currentSession + "/" + sessionRecipient.sessionID + feedbackDetails;
        String basicFeedback = "/basicFeedback";
        currentPath = database.getReference(currentFeedbackPath + basicFeedback);
        currentPath.setValue(tag);
    }

    public void updateFeedbackString(String feedbackText) {
        if (feedbackText.length() > 0) {
            String currentFeedbackPath = userID + currentSession + "/" + sessionRecipient.sessionID + feedbackDetails;
            String feedbackStringPath = "/furtherFeedback";
            currentPath = database.getReference(currentFeedbackPath + feedbackStringPath);
            currentPath.setValue(feedbackText);
        }
    }

    public void uploadImage(Uri imageRef){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference userStorageRef = storageReference.child(userID + "/" + sessionRecipient.sessionID);
        UploadTask uploadTask = userStorageRef.putFile(imageRef);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Image", "Image upload success");
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                CreateLetterActivity.imageUploadProgress = taskSnapshot.getBytesTransferred();
            }
        });
    }

    public void updateCurrentSessionRecipient(SessionRecipient sessionRecipient){
        String recipData = "/recipientdata";
        String currentRecipientPath = userID + currentSession + "/" + sessionRecipient.sessionID + recipData;
        currentPath = database.getReference(currentRecipientPath);
        currentPath.setValue(sessionRecipient);
    }

    public void updateLetterDetails(MainLetterDetails mainLetterDetails) {
        String letterDetails = "/letterDetails";
        String currentLetterDetailsPath = userID + currentSession + "/" + sessionRecipient.sessionID + letterDetails;
        currentPath = database.getReference(currentLetterDetailsPath);
        currentPath.setValue(mainLetterDetails);
    }

    public void updateDeliveryMethodDetails(Boolean paid) {
        String deliveryDetails = "/deliveryDetailsPaid";
        String currentLetterDeliveryPath = userID + currentSession + "/" + sessionRecipient.sessionID + deliveryDetails;
        currentPath = database.getReference(currentLetterDeliveryPath);
        currentPath.setValue(paid);
    }

}
