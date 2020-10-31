package com.josef.mobile.data.local.db;

import android.content.Context;
import android.util.Log;

import com.josef.mobile.ui.main.archive.model.Archive;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
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

        return mAppDatabase.archiveDao().loadAll();
    }

    public Single<Archive> findbyName(final Archive archive) {
        return mAppDatabase.archiveDao().findByName(archive.name);
    }

    public Completable insertArchive(final Archive archive) {
        return mAppDatabase.archiveDao().insertUser(archive);
    }

    public Flowable<Archive> getArchive() {
        return mAppDatabase.archiveDao().getArchive();
    }

    @Override
    public void deleteArchives(Archive archive) {
        mAppDatabase.archiveDao().delete(archive);
    }

    @Override
    public void insertArchives(Archive archive) {
        mAppDatabase.archiveDao().insert(archive);
    }

}
