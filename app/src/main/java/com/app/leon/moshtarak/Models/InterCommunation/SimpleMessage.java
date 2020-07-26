package com.app.leon.moshtarak.Models.InterCommunation;

import com.google.gson.annotations.SerializedName;

public class SimpleMessage {
    @SerializedName("message")
    String message;

    public SimpleMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}