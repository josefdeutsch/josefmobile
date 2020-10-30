package com.josef.mobile.data.local.db;


import com.josef.mobile.ui.main.archive.model.Archive;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DbHelper {

    Flowable<List<Archive>> getAllArchives();

    Single<Archive> findbyName(final Archive archive);

    void insertArchives(final Archive archive);

    void deleteArchives(final Archive archive);

    Completable insertArchive(final Archive archive);

    Flowable<Archive> getArchive();
}
