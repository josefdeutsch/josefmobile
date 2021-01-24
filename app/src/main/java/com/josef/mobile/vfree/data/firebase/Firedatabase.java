package com.josef.mobile.vfree.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Firedatabase implements Firebase {

    private final FirebaseDatabase database;

    @Inject
    public Firedatabase(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public DatabaseReference getDataBaseRefChild_User() {
        return database.getReference("users");
    }

    @Override
    public DatabaseReference getDataBaseRefChild_Profile() {
        return database.getReference("profile");
    }

    @Override
    public FirebaseDatabase getFirebaseDataBase() {
        return database;
    }
}
