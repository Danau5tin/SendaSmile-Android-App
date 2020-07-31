package com.firstapp.helpapp.helper;

import com.google.firebase.database.Exclude;

public class SessionRecipient {

    Boolean keyWorker =  null;
    Boolean lady = null;
    String locality = "NA";

    @Exclude
    int sessionID = 1;

    public SessionRecipient() {}

    public Boolean getKeyWorker() {
        return keyWorker;
    }

    public void setKeyWorker(Boolean keyWorker) {
        this.keyWorker = keyWorker;
    }

    public Boolean getLady() {
        return lady;
    }

    public void setLady(Boolean lady) {
        this.lady = lady;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public Boolean isCompleted() {
        return keyWorker != null && lady != null;
    }
}
