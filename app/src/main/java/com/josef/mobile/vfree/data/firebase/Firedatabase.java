package com.josef.mobile.vfree.data.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class Firedatabase implements Firebase {

    @Nullable
    private final FirebaseDatabase database;

    @Inject
    public Firedatabase(@Nullable FirebaseDatabase database)
    {
        this.database = database;
    }

    @Nullable
    @Override
    public DatabaseReference getDataBaseRefChild_User()
    {
        return Objects.requireNonNull(database.getReference("users"), "db reference users must not be null" );
    }

    @Nullable
    @Override
    public DatabaseReference getDataBaseRefChild_Profile() throws NullPointerException
    {
        return Objects.requireNonNull(database.getReference("profile"),"db reference profile must not be null" );
    }

    @Nullable
    @Override
    public FirebaseDatabase getFirebaseDataBase() throws NullPointerException
    {
        return Objects.requireNonNull(database,"db raw must not be null" );
    }
}
