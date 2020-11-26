package com.josef.mobile.data.local.db;


import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.db.model.LocalCache;

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
