package com.josef.mobile.vfree.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.vfree.data.firebase.Firebase;
import com.josef.mobile.vfree.data.local.db.DbHelper;
import com.josef.mobile.vfree.data.local.db.model.Archive;
import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.data.local.prefs.PreferencesHelper;
import com.josef.mobile.vfree.data.remote.Endpoints;
import com.josef.mobile.vfree.data.remote.model.Endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class AppDataManager implements DataManager {

    private final DbHelper dbHelper;
    private final Endpoints endpoints;
    private final PreferencesHelper preferencesHelper;
    private final Firebase firebase;

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
    public DatabaseReference getDataBaseRefChild_User() {
        return firebase.getDataBaseRefChild_User();
    }

    @Override
    public DatabaseReference getDataBaseRefChild_Profile() {
        return firebase.getDataBaseRefChild_Profile();
    }

    @Override
    public FirebaseDatabase getFirebaseDataBase() {
        return firebase.getFirebaseDataBase();
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
