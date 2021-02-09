package com.josef.mobile.vfree.data.local.db;


import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DbHelper {

    @NonNull
    Flowable<List<Archive>> getAllArchives();

    @NonNull
    Single<Archive> findArchiveByName(@NonNull final Archive archive);

    void insertArchives(@NonNull final Archive archive);

    void deleteArchives(@NonNull final Archive archive);

    void updateArchives(@NonNull final Archive archive);

    @NonNull
    Flowable<Archive> getArchive();

    @NonNull
    Flowable<List<LocalCache>> getAllEndpoints();

    @NonNull
    Single<LocalCache> findEndpointsByName(@NonNull final LocalCache endpoints);

    void insertEndpoints(@NonNull final LocalCache endpoints);

    void insertAllEndpoints(@NonNull final List<LocalCache> endpoints);

    void deleteEndpoints(@NonNull final LocalCache endpoints);

    void updateEndpoints(@NonNull final LocalCache endpoints);

    @NonNull
    Flowable<LocalCache> getEndpoint();
}
