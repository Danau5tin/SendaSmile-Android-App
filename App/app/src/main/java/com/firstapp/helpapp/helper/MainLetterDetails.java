package com.firstapp.helpapp.helper;

import com.google.firebase.database.Exclude;

import static com.firstapp.helpapp.CreateLetterActivity.imageUploadProgress;
import static com.firstapp.helpapp.helper.MainLetterDetails.LETTER_RESULT.*;

public class MainLetterDetails {

    @Exclude
    Boolean deliveryChosen = false;
    @Exclude
    Boolean imageRequired = true;
    @Exclude
    Boolean imageChosen = false;

    String mainText;

    public enum LETTER_RESULT {
        SUCCESS, NO_DELIVERY, SMALL_TEXT, NO_IMAGE, UPLOADING_IMG
    }


    public Boolean getDeliveryChosen() {
        return deliveryChosen;
    }

    public void setDeliveryChosen(Boolean deliveryChosen) {
        this.deliveryChosen = deliveryChosen;
    }

    public Boolean getImageRequired() {
        return imageRequired;
    }

    public void setImageRequired(Boolean imageRequired) {
        this.imageRequired = imageRequired;
    }

    public Boolean getImageChosen() {
        return imageChosen;
    }

    public void setImageChosen(Boolean imageChosen) {
        this.imageChosen = imageChosen;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public LETTER_RESULT isOkayToUpload() {
        if (textIsNotLongEnough()) {
            return SMALL_TEXT;
        }
        if (!deliveryChosen){
            return NO_DELIVERY;
        }
        if (imageRequired) {
            if (imageChosen) {
                if (imageIsUploaded()) {
                    return SUCCESS;
                } else {
                    return UPLOADING_IMG;
                }
            } else {
                return NO_IMAGE;
            }
        } else {
            return SUCCESS;
        }
        }

    private boolean imageIsUploaded() {
        return imageUploadProgress >= 95;
    }

    private boolean textIsNotLongEnough() {
        return mainText.length() <= 30;
}
}
