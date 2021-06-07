package com.josef.mobile.vfree.ui.main.impr.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public final class Impression {


    @Nullable
    @SerializedName("id")
    public long id;
    @Nullable
    @SerializedName("header")
    public String header;
    @Nullable
    @SerializedName("desc")
    public String desc;
    @Nullable
    @SerializedName("url")
    public String url;

    public Impression() {

    }

    public Impression(@Nullable String header,
                      @Nullable String desc,
                      @Nullable String url) {
        this.header = header;
        this.desc = desc;
        this.url = url;
    }

    @Nullable
    public String getHeader() {
        return Objects.requireNonNull(header,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "header must not be null");
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Nullable
    public String getDesc() {
        return Objects.requireNonNull(desc,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "desc must not be null");
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Nullable
    public String getUrl() {
        return Objects.requireNonNull(url,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "url must not be null");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Nullable
    public Long getId() {
        return Objects.requireNonNull(id,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "id must not be null" );
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String exception;
    @NonNull
    public String getException() {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }
}
