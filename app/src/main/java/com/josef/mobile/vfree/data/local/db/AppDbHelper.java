package com.josef.mobile.vfree.data.local.db;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public final class AppDbHelper implements DbHelper {

    private final AppDataBase mAppDatabase;

    @Inject
    public AppDbHelper(@NonNull AppDataBase appDatabase) {

        this.mAppDatabase = appDatabase;
    }

    @NonNull
    @Override
    public Flowable<List<Archive>> getAllArchives() {
        return mAppDatabase.archiveDao().loadAllArchives();
    }

    @NonNull
    public Single<Archive> findArchiveByName(@NonNull final Archive archive) {
        return mAppDatabase.archiveDao().findByName(archive.name);
    }

    @NonNull
    public Flowable<Archive> getArchive() {
        return mAppDatabase.archiveDao().getArchive();
    }

    @Override
    public void deleteArchives(@NonNull Archive archive) {
        mAppDatabase.archiveDao().delete(archive);
    }

    @Override
    public void updateArchives(@NonNull Archive archive) {
        mAppDatabase.archiveDao().update(archive);
    }

    @Override
    public void insertArchives(@NonNull Archive archive) {
        mAppDatabase.archiveDao().insert(archive);
    }

    @NonNull
    @Override
    public Flowable<List<LocalCache>> getAllEndpoints() {
        return mAppDatabase.endpontsDao().loadAllEndpoints();
    }

    @NonNull
    @Override
    public Single<LocalCache> findEndpointsByName(@NonNull LocalCache endpoints) {
        return mAppDatabase.endpontsDao().findEndpointByName(endpoints.name);
    }

    @Override
    public void insertEndpoints(@NonNull LocalCache localCache) {
        mAppDatabase.endpontsDao().insert(localCache);
    }

    @Override
    public void insertAllEndpoints(@NonNull List<LocalCache> localCache) {
        mAppDatabase.endpontsDao().insertAllEndpoints(localCache);
    }

    @Override
    public void deleteEndpoints(@NonNull LocalCache localCache) {
        mAppDatabase.endpontsDao().delete(localCache);
    }

    @Override
    public void updateEndpoints(@NonNull LocalCache localCache) {
        mAppDatabase.endpontsDao().update(localCache);
    }

    @NonNull
    @Override
    public Flowable<LocalCache> getEndpoint() {
        return mAppDatabase.endpontsDao().getEndpoint();
    }
}
