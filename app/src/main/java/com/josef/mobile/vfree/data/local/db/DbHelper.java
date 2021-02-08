package com.josef.mobile.vfree.data.local.db;


import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DbHelper {

    Flowable<List<Archive>> getAllArchives();

    Single<Archive> findArchiveByName(final Archive archive);

    void insertArchives(final Archive archive);

    void deleteArchives(final Archive archive);

    void updateArchives(final Archive archive);

    Flowable<Archive> getArchive();


    Flowable<List<LocalCache>> getAllEndpoints();

    Single<LocalCache> findEndpointsByName(final LocalCache endpoints);

    void insertEndpoints(final LocalCache endpoints);

    void insertAllEndpoints(final List<LocalCache> endpoints);

    void deleteEndpoints(final LocalCache endpoints);

    void updateEndpoints(final LocalCache endpoints);

    Flowable<LocalCache> getEndpoint();
}
