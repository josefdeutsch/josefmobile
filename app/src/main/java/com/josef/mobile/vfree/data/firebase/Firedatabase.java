package com.josef.mobile.vfree.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.josef.mobile.vfree.ui.auth.model.User;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class Firedatabase implements Firebase {

    private static final String TAG = "Firedatabase";

    @Nullable
    private final FirebaseDatabase database;

    @Inject
    public Firedatabase(@Nullable FirebaseDatabase database) {
        this.database = database;
    }

    @Nullable
    @Override
    public DatabaseReference getDataBaseRefChild_User() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        return myRef;
    }

    @Nullable
    @Override
    public DatabaseReference getDataBaseRefChild_Profile() throws NullPointerException {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("profile");

        return myRef;
    }

    @Nullable
    @Override
    public FirebaseDatabase getFirebaseDataBase() throws NullPointerException {
        return Objects.requireNonNull(database, "db raw must not be null");
    }
}
