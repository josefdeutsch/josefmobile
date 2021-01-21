package com.josef.mobile.vfree.data.firebase;

import com.google.firebase.database.DatabaseReference;

public interface Firebase {

    DatabaseReference getDataBaseRefChild_User();

    DatabaseReference getDataBaseRefChild_Profile();
}
