package com.josef.mobile.vfree.ui.main.profiles.model;


import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public final class Profile {

    @Nullable
    @SerializedName("header")
    private String name;
    @Nullable
    @SerializedName("subheader")
    private String subheader;
    @Nullable
    @SerializedName("subheader2")
    private String subheader2;
    @Nullable
    @SerializedName("article")
    private String article;
    @Nullable
    @SerializedName("desc")
    private String desc;
    @Nullable
    @SerializedName("id")
    private long id;

    @NonNull
    private boolean isShrink = true;
    @NonNull
    public String exception;


    public Profile() {

    }

    public Profile(
            @Nullable String header,
            @Nullable String subheader,
            @Nullable String subheader2,
            @Nullable String article,
            @Nullable String desc) {

        this.name = header;
        this.subheader = subheader;
        this.subheader2 = subheader2;
        this.article = article;
        this.desc = desc;
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
    public String getSubheader() {
        return Objects.requireNonNull(subheader,
                "com.josef.mobile.vfree.ui.main.about.model.About About subheader must not be null");
    }

    public void setSubheader(@Nullable String subheader) {
        this.subheader = subheader;
    }

    @Nullable
    public String getSubheader2() {
        return  Objects.requireNonNull(subheader,
                "com.josef.mobile.vfree.ui.main.about.model.About About subheader must not be null");
    }

    public void setSubheader2(@Nullable String subheader2) {
        this.subheader2 = subheader2;
    }

    @Nullable
    public String getArticle() {
        return Objects.requireNonNull(article,
                "com.josef.mobile.vfree.ui.main.about.model.About About article must not be null");
    }

    public void setArticle(@Nullable String article) {
        this.article = article;
    }

    public void setTitle(@Nullable String title) {
        this.article = title;
    }

    @Nullable
    public String getDesc() {
        return Objects.requireNonNull(desc,
                "com.josef.mobile.vfree.ui.main.about.model.About About description must not be null");
    }

    public void setDesc(@Nullable String desc) {
        this.desc = desc;
    }

    @NonNull
    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(@NonNull boolean shrink) {
        isShrink = shrink;
    }

    @Nullable
    public long getId() {
        return Objects.requireNonNull(id,
                "com.josef.mobile.vfree.ui.main.about.model.About About id must not be null");
    }

    public void setId(@Nullable long id) {
        this.id = id;
    }

    @NonNull
    public String getException() {
        return exception;
    }

    public void setException(@NonNull String exception) {
        this.exception = exception;
    }
}

