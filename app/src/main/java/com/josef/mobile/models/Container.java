package com.josef.mobile.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Container {

    @SerializedName("header")
    @Expose
    private String header;

    @SerializedName("subheader")
    @Expose
    private String subheader;

    @SerializedName("png")
    @Expose
    private String png;

    @SerializedName("url")
    @Expose
    private String url;

    public Container(String header, String subheader, String png, String url) {
        this.header = header;
        this.subheader = subheader;
        this.png = png;
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSubheader() {
        return subheader;
    }

    public void setSubheader(String subheader) {
        this.subheader = subheader;
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

    private boolean isChecked;

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
