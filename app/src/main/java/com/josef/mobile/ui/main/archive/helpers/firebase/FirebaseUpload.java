package com.josef.mobile.ui.main.archive.helpers.firebase;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.ui.main.MainActivity;

import io.reactivex.Observable;

public interface FirebaseUpload {

      Observable<DatabaseReference> synchronize(MainActivity mainActivity);
}
