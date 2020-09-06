package com.josef.mobile.free.ui.body;

import com.google.android.exoplayer2.Player;

public class Data {

    String png;
    String url;
    String email;
    String color;

    public Data() {

    }

    public Data(String color) {
        this.color = color;
    }

    public Data(String png, String url, String email) {
        this.png = png;
        this.url = url;
        this.email = email;

    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String png) {
        this.color = color;
    }




}
