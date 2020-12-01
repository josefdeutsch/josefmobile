package com.josef.mobile.ui.main.archive.fire.model;

public class Data {

    String png;
    String url;
    String email;

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


}
