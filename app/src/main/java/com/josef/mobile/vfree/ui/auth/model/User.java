package com.josef.mobile.vfree.ui.auth.model;


import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;


public final class User {

    @NonNull
    public int id;

    @NonNull
    @PropertyName("uid")
    public String uid;

    @NonNull
    @PropertyName("email")
    public String email;

    @NonNull
    @PropertyName("fname")
    public String fname;

    @NonNull
    @PropertyName("lname")
    public String lname;

    @NonNull
    @PropertyName("photoUrl")
    public String photoUrl;

    @NonNull
    public Throwable throwable;

    public User(){


    }

    public User(@NonNull String email, @NonNull String fname, @NonNull String lname) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @PropertyName("uid")
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    @PropertyName("fname")
    public String getFname() {
        return fname;
    }

    public void setFname(@NonNull String fname) {
        this.fname = fname;
    }

    @NonNull
    @PropertyName("lname")
    public String getLname() {
        return lname;
    }

    public void setLname(@NonNull String lname) {
        this.lname = lname;
    }

    @NonNull
    @PropertyName("photoUrl")
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(@NonNull String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @NonNull
    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(@NonNull Throwable throwable) {
        this.throwable = throwable;
    }
}