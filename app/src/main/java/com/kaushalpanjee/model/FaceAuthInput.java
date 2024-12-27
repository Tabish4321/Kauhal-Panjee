package com.kaushalpanjee.model;

public class FaceAuthInput {

    public String input;
    public String url;

    public FaceAuthInput() {
    }

    public FaceAuthInput(String input, String url) {
        this.input = input;
        this.url = url;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
