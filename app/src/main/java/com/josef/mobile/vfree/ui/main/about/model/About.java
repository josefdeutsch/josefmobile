package com.josef.mobile.vfree.ui.main.about.model;


public class About {

    private String header;
    private String subheader;
    private String subheader2;
    private String article;
    private String desc;
    private long id;
    private boolean isShrink = true;
    public String exception;


    public About(){

    }

    public About(String header, String subheader, String subheader2, String article, String desc) {
        this.header = header;
        this.subheader = subheader;
        this.subheader2 = subheader2;
        this.article = article;
        this.desc = desc;
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

    public String getSubheader2() {
        return subheader2;
    }

    public void setSubheader2(String subheader2) {
        this.subheader2 = subheader2;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setTitle(String title) {
        this.article = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}

