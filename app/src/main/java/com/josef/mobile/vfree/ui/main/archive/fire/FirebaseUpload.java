package com.josef.mobile.vfree.ui.main.archive.fire;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.vfree.ui.main.MainActivity;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public interface FirebaseUpload {

    String _ = "";
    String SOURCES = "sources";
    String CARD = "card";
    String BACKGROUND = "background";
    String TITLE = "title";
    String STUDIO = "studio";
    String CATEGORY = "category";
    String VALUE = "Artwork";
    String GOOGLE_VIDEO = "googlevideos";
    String VIDEO = "videos";
    String DESCRIPTION = "description";

    @NonNull
    Observable<DatabaseReference> synchronize(@NonNull MainActivity mainActivity);
}
