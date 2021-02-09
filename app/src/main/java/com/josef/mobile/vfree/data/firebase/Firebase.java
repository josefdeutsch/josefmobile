package com.josef.mobile.vfree.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

public interface Firebase {

    @Nullable
    DatabaseReference getDataBaseRefChild_User();

    @Nullable
    DatabaseReference getDataBaseRefChild_Profile();

    @Nullable
    FirebaseDatabase getFirebaseDataBase();
}
