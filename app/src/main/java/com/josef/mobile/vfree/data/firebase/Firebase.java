package com.josef.mobile.vfree.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public interface Firebase {

    DatabaseReference getDataBaseRefChild_User();

    DatabaseReference getDataBaseRefChild_Profile();

    FirebaseDatabase getFirebaseDataBase();
}
