package com.josef.mobile.ui.main.archive.helpers.local;

import androidx.lifecycle.LiveData;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public interface ArchiveDatabase {

    LiveData<Resource<List<Archive>>> observeArchive();

    void deleteArchives(final Archive archive, CompositeDisposable compositeDisposable);

    void deleteArchivesPref(final Archive archive, CompositeDisposable compositeDisposable);
}
