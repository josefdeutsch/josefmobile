package com.josef.mobile.data.local.db;


import com.josef.mobile.data.local.db.dao.Archive;

import java.util.List;

import io.reactivex.Flowable;

public interface DbHelper {

    Flowable<List<Archive>> getAllArchives();

    void insertArchives(final Archive archive);

    void deleteArchives(final Archive archive);
}
