package com.josef.mobile.data.local.db;

import android.content.Context;
import android.util.Log;

import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.db.model.LocalCache;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class AppDbHelper implements DbHelper {

    private static final String TAG = "AppDbHelper";
    private final AppDataBase mAppDatabase;
    private final Context context;

    @Inject
    public AppDbHelper(AppDataBase appDatabase, Context context) {
        Log.d(TAG, "AppDbHelper: " + appDatabase);
        this.mAppDatabase = appDatabase;
        this.context = context;
    }

    @Override
    public Flowable<List<Archive>> getAllArchives() {
        return mAppDatabase.archiveDao().loadAllArchives();
    }

    public Single<Archive> findArchiveByName(final Archive archive) {
        return mAppDatabase.archiveDao().findByName(archive.name);
    }

    public Flowable<Archive> getArchive() {
        return mAppDatabase.archiveDao().getArchive();
    }

    @Override
    public void deleteArchives(Archive archive) {
        mAppDatabase.archiveDao().delete(archive);
    }

    @Override
    public void updateArchives(Archive archive) {
        mAppDatabase.archiveDao().update(archive);
    }

    @Override
    public void insertArchives(Archive archive) {
        mAppDatabase.archiveDao().insert(archive);
    }


    @Override
    public Flowable<List<LocalCache>> getAllEndpoints() {
        return mAppDatabase.endpontsDao().loadAllEndpoints();
    }

    @Override
    public Single<LocalCache> findEndpointsByName(LocalCache endpoints) {
        return mAppDatabase.endpontsDao().findEndpointByName(endpoints.name);
    }

    @Override
    public void insertEndpoints(LocalCache localCache) {
        mAppDatabase.endpontsDao().insert(localCache);
    }

    @Override
    public void insertAllEndpoints(List<LocalCache> localCache) {
        mAppDatabase.endpontsDao().insertAllEndpoints(localCache);
    }

    @Override
    public void deleteEndpoints(LocalCache localCache) {
        mAppDatabase.endpontsDao().delete(localCache);
    }

    @Override
    public void updateEndpoints(LocalCache localCache) {
        mAppDatabase.endpontsDao().update(localCache);
    }

    @Override
    public Flowable<LocalCache> getEndpoint() {
        return mAppDatabase.endpontsDao().getEndpoint();
    }
}
