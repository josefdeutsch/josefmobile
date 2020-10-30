package com.josef.mobile.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Firedatabase implements Firebase {

    private static final String TAG = "Firedatabase";
    FirebaseDatabase database;

    @Inject
    public Firedatabase(FirebaseDatabase database) {
        this.database = FirebaseDatabase.getInstance();
    }

    public DatabaseReference getDatabasereference() {
        return database.getReference("users");
    }
}
