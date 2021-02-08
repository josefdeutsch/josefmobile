package com.josef.mobile.vfree.ui.main.home.model;

public class Profile {

    private String url;
    private String article;
    private long id;

    public String exception;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Profile() {
    }

    public Profile(String url, String article) {
        this.url = url;
        this.article = article;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
