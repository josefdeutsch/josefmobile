package com.josef.mobile.data.local;

import android.util.Log;

import com.josef.mobile.data.local.db.DbHelper;
import com.josef.mobile.data.local.db.dao.Archive;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "DataManager";

    DbHelper dbHelper;

    @Inject
    public AppDataManager(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Flowable<List<Archive>> getAllArchives() {

        return dbHelper.getAllArchives();
    }


    public void insertArchives(final Archive archive) {
        Log.d(TAG, "insertArchives: Datamanger ");
        dbHelper.insertArchives(archive);
    }

    @Override
    public void deleteArchives(Archive archive) {
        dbHelper.deleteArchives(archive);
    }


}
