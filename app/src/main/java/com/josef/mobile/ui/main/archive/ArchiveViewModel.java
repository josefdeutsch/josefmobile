package com.josef.mobile.ui.main.archive;


import androidx.lifecycle.LiveData;

import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.helpers.firebase.FirebaseUpload;
import com.josef.mobile.ui.main.archive.helpers.local.ArchiveDatabase;
import com.josef.mobile.ui.main.archive.model.Archive;

import java.util.List;

import javax.inject.Inject;

public class ArchiveViewModel extends BaseViewModel {

    private static final String TAG = "PostsViewModel";

    private final FirebaseUpload firebaseUpload;
    private final ArchiveDatabase archiveDatabase;

    @Inject
    public ArchiveViewModel(FirebaseUpload firebaseUpload, ArchiveDatabase archiveDatabase) {
        this.firebaseUpload = firebaseUpload;
        this.archiveDatabase = archiveDatabase;
    }

    public LiveData<Resource<List<Archive>>> observeArchive() {
        return archiveDatabase.observeArchive();
    }

    public void synchronize(MainActivity mainActivity) {
        firebaseUpload.synchronize(mainActivity, compositeDisposable);
    }

    public void deleteArchivesPref(final Archive archive) {
        archiveDatabase.deleteArchivesPref(archive, compositeDisposable);
    }

    public void deleteArchives(final Archive archive) {
        archiveDatabase.deleteArchives(archive, compositeDisposable);
    }
}


