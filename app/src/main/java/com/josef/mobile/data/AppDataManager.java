package com.josef.mobile.data;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.data.firebase.Firebase;
import com.josef.mobile.data.local.db.DbHelper;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.prefs.PreferencesHelper;
import com.josef.mobile.data.remote.Endpoints;
import com.josef.mobile.data.remote.model.Endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
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
    public Single<Archive> findbyName(Archive archive) {
        return dbHelper.findbyName(archive);
    }

    public void insertArchives(final Archive archive) {
        dbHelper.insertArchives(archive);
    }

    @Override
    public void deleteArchives(Archive archive) {
        dbHelper.deleteArchives(archive);
    }

    @Override
    public Completable insertArchive(Archive archive) {
        return dbHelper.insertArchive(archive);
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
    public String getHashString() {
        return preferencesHelper.getHashString();
    }

    @Override
    public void setHashString(String string) {
        preferencesHelper.setHashString(string);
    }
}
