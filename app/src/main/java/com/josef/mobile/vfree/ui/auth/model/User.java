package com.josef.mobile.vfree.ui.auth.model;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;
import java.io.Serializable;
import java.util.Objects;

public final class User implements Serializable {

    @NonNull
    public int id;
    @Nullable
    @SerializedName("uid")
    public String uid;
    @Nullable
    @SerializedName("email")
    public String email;
    @Nullable
    @SerializedName("fname")
    public String fname;
    @Nullable
    @SerializedName("lname")
    public String lname;
    @Nullable
    @SerializedName("photoUrl")
    public String photoUrl;

    @NonNull
    public Throwable throwable;

    public User() {

    }

    public User(@Nullable String uid,
                @Nullable String email,
                @Nullable String fname,
                @Nullable String photoUrl)
    {
        this.uid = uid;
        this.email = email;
        this.fname = fname;
        this.photoUrl = photoUrl;
    }

    @NonNull
    public Throwable getThrowable() {
        return throwable;
    }

    public int getId() {
        Objects.requireNonNull(id,
                "com.josef.mobile.vfree.ui.auth.model.User id must not be null");
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getUid() {
        Objects.requireNonNull(uid,
                "com.josef.mobile.vfree.ui.auth.model.User uid must not be null" );
        return uid;
    }

    public void setUid(@Nullable String uid) {
        this.uid = uid;
    }

    @Nullable
    public String getEmail() {
        return Objects.requireNonNull(email,
                "com.josef.mobile.vfree.ui.auth.model.User email mmust not be null" );
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getFname() {
        return Objects.requireNonNull(fname,
                "com.josef.mobile.vfree.ui.auth.model.User first name must not be null" );
    }

    public void setFname(@Nullable String fname) {
        this.fname = fname;
    }

    @Nullable
    public String getLname() {
        return Objects.requireNonNull(lname,
                "com.josef.mobile.vfree.ui.auth.model.User last name must not be null" );
    }

    public void setLname(@Nullable String lname) {
        this.lname = lname;
    }

    @Nullable
    public String getPhotoUrl() {
        return Objects.requireNonNull(photoUrl,
                "com.josef.mobile.vfree.ui.auth.model.User photourl must not be null" );
    }

    public void setPhotoUrl(@Nullable String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setThrowable(@NonNull Throwable throwable) {
        this.throwable = throwable;
    }
}