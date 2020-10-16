package com.josef.mobile.ui.auth;


import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String uid, name, photoUrl;
    @SuppressWarnings("WeakerAccess")
    public String email;
    public Date createdAt;
    public boolean hasUSER;

    public User() {
    }

    public User(String uid, String name, String email, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}