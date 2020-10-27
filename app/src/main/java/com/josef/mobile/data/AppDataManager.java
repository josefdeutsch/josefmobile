package com.josef.mobile.data;

import com.josef.mobile.data.local.db.DbHelper;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.prefs.PreferencesHelper;
import com.josef.mobile.data.remote.Endpoints;
import com.josef.mobile.data.remote.model.Endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class AppDataManager implements DataManager {


    DbHelper dbHelper;
    Endpoints endpoints;
    PreferencesHelper preferencesHelper;

    @Inject
    public AppDataManager(DbHelper dbHelper, Endpoints endpoints, PreferencesHelper preferencesHelper) {
        this.dbHelper = dbHelper;
        this.endpoints = endpoints;
        this.preferencesHelper = preferencesHelper;
    }

    public Flowable<List<Archive>> getAllArchives() {
        return dbHelper.getAllArchives();
    }


    public void insertArchives(final Archive archive) {
        dbHelper.insertArchives(archive);
    }

    @Override
    public void deleteArchives(Archive archive) {
        dbHelper.deleteArchives(archive);
    }


    @Override
    public Flowable<Endpoint> getChange(String url) {
        return endpoints.getChange(url);
    }

    @Override
    public String getHashString() {
        return preferencesHelper.getHashString();
    }

    @Override
    public void setHashString(String string) {
        preferencesHelper.setHashString(string);
    }
}
