package com.josef.mobile.data;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.data.firebase.Firebase;
import com.josef.mobile.data.local.db.DbHelper;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.data.local.prefs.PreferencesHelper;
import com.josef.mobile.data.remote.Endpoints;
import com.josef.mobile.data.remote.model.Endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class AppDataManager implements DataManager {

    DbHelper dbHelper;
    Endpoints endpoints;
    PreferencesHelper preferencesHelper;
    Firebase firebase;

    @Inject
    public AppDataManager(DbHelper dbHelper, Endpoints endpoints, Firebase firebase, PreferencesHelper preferencesHelper) {
        this.dbHelper = dbHelper;
        this.endpoints = endpoints;
        this.preferencesHelper = preferencesHelper;
        this.firebase = firebase;
    }

    public Flowable<List<Archive>> getAllArchives() {
        return dbHelper.getAllArchives();
    }

    @Override
    public Single<Archive> findArchiveByName(Archive archive) {
        return dbHelper.findArchiveByName(archive);
    }

    public void insertArchives(final Archive archive) {
        dbHelper.insertArchives(archive);
    }

    @Override
    public void deleteArchives(Archive archive) {
        dbHelper.deleteArchives(archive);
    }

    @Override
    public void updateArchives(Archive archive) {
        dbHelper.updateArchives(archive);
    }

    @Override
    public Flowable<Archive> getArchive() {
        return dbHelper.getArchive();
    }

    @Override
    public Flowable<Endpoint> getChange(String url) {
        return endpoints.getChange(url);
    }

    @Override
    public DatabaseReference getDatabasereference() {
        return firebase.getDatabasereference();
    }

    @Override
    public Flowable<List<LocalCache>> getAllEndpoints() {
        return dbHelper.getAllEndpoints();
    }

    @Override
    public Single<LocalCache> findEndpointsByName(LocalCache localCache) {
        return dbHelper.findEndpointsByName(localCache);
    }

    @Override
    public void insertEndpoints(LocalCache localCache) {
        dbHelper.insertEndpoints(localCache);
    }

    @Override
    public void insertAllEndpoints(List<LocalCache> localCache) {
        dbHelper.insertAllEndpoints(localCache);
    }

    @Override
    public void deleteEndpoints(LocalCache localCache) {
        dbHelper.deleteEndpoints(localCache);
    }

    @Override
    public void updateEndpoints(LocalCache localCache) {
        dbHelper.updateEndpoints(localCache);
    }

    @Override
    public Flowable<LocalCache> getEndpoint() {
        return dbHelper.getEndpoint();
    }

    @Override
    public String getHashMapArchiveIndicator() {
        return preferencesHelper.getHashMapArchiveIndicator();
    }

    @Override
    public void setHashMapArchiveIndicator(String string) {
        preferencesHelper.setHashMapArchiveIndicator(string);
    }

    @Override
    public void clearHashmapIndicator() {
        preferencesHelper.clearHashmapIndicator();
    }
}
