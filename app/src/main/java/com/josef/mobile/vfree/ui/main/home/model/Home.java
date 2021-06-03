package com.josef.mobile.vfree.ui.main.home.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public final class Home {

    @Nullable
    @SerializedName("url")
    private String url;
    @Nullable
    @SerializedName("article")
    private String article;
    @Nullable
    @SerializedName("id")
    private long id;

    public String exception;

    public String getException() {
        return exception;
    }

    public void setException(@Nullable String exception) {
        this.exception = exception;
    }

    public Home() {
    }

    public Home(@Nullable String url,
                @Nullable String article) {
        this.url = url;
        this.article = article;
    }

    @Nullable
    public String getUrl() {
        return Objects.requireNonNull(url,
                "com.josef.mobile.vfree.ui.main.home.model.Profile " +
                        "Profile url must not be null");
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getArticle() {
        return Objects.requireNonNull(article,
                "com.josef.mobile.vfree.ui.main.home.model.Profile " +
                        "Profile article must not be null");
    }

    public void setArticle(@Nullable String article) {
        this.article = article;
    }

    public long getId() {
        return  Objects.requireNonNull(id,
                "com.josef.mobile.vfree.ui.main.home.model.Profile " +
                        "Profile id must not be null");
    }

    public void setId(long id) {
        this.id = id;
    }
}
