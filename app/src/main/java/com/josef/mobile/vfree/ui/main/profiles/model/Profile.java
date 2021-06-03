package com.josef.mobile.vfree.ui.main.profiles.model;


import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public final class Profile {

    @Nullable
    @SerializedName("name")
    private String name;
    @Nullable
    @SerializedName("url_logo")
    private String url_logo;
    @Nullable
    @SerializedName("url_content")
    private String url_content;
    @NonNull
    public String exception;
    @Nullable
    @SerializedName("id")
    private long id;

    public Profile() {

    }

    public Profile(
            @Nullable String header,
            @Nullable String url_logo,
            @Nullable String url_content
         ) {

        this.name = header;
        this.url_logo = url_logo;
        this.url_content = url_content;
    }

    @Nullable
    public String getName() {
        return Objects.requireNonNull(name,
                "com.josef.mobile.vfree.ui.main.about.model.About About header must not be null");
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getUrl_logo() {
        return Objects.requireNonNull(url_logo,
                "com.josef.mobile.vfree.ui.main.about.model.About About subheader must not be null");
    }

    public void setUrl_logo(@Nullable String url_logo) {
        this.url_logo = url_logo;
    }

    @Nullable
    public String getSubheader2() {
        return  Objects.requireNonNull(url_logo,
                "com.josef.mobile.vfree.ui.main.about.model.About About subheader must not be null");
    }

    @Nullable
    public String getUrl_content() {
        return Objects.requireNonNull(url_content,
                "com.josef.mobile.vfree.ui.main.about.model.About About article must not be null");
    }
    @Nullable
    public long getId() {
        return Objects.requireNonNull(id,
                "com.josef.mobile.vfree.ui.main.about.model.About About id must not be null");
    }

    public void setId(@Nullable long id) {
        this.id = id;
    }
    public void setUrl_content(@Nullable String url_content) {
        this.url_content = url_content;
    }

    public void setTitle(@Nullable String title) {
        this.url_content = title;
    }


    @NonNull
    public String getException() {
        return exception;
    }

    public void setException(@NonNull String exception) {
        this.exception = exception;
    }
}

