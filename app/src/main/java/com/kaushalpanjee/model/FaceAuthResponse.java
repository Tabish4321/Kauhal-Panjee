package com.kaushalpanjee.model;

import com.google.gson.annotations.SerializedName;

public class FaceAuthResponse {

    @SerializedName("PostOnAUA_Face_authResult")
    private String result;

    public FaceAuthResponse() {
    }

    public FaceAuthResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}