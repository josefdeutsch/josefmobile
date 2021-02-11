package com.josef.mobile.vfree.data;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.vfree.data.ads.AdsRequest;
import com.josef.mobile.vfree.data.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.data.firebase.Firebase;
import com.josef.mobile.vfree.data.local.db.DbHelper;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.data.local.prefs.PreferencesHelper;
import com.josef.mobile.vfree.data.remote.Endpoints;
import com.josef.mobile.vfree.data.remote.model.Endpoint;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public final class AppDataManager implements DataManager {

    @NonNull
    private final DbHelper dbHelper;
    @NonNull
    private final Endpoints endpoints;
    @NonNull
    private final PreferencesHelper preferencesHelper;
    @NonNull
    private final Firebase firebase;
    @NonNull
    private final AdsRequest adsRequest;

    @Inject
    public AppDataManager(@NonNull DbHelper dbHelper,
                          @NonNull Endpoints endpoints,
                          @NonNull Firebase firebase,
                          @NonNull PreferencesHelper preferencesHelper,
                          @NonNull AdsRequest adsRequest
    ) {
        this.dbHelper = dbHelper;
        this.endpoints = endpoints;
        this.preferencesHelper = preferencesHelper;
        this.firebase = firebase;
        this.adsRequest = adsRequest;
    }

    @NonNull
    public Flowable<List<Archive>> getAllArchives() {
        return dbHelper.getAllArchives();
    }

    @Override
    public Single<Archive> findArchiveByName(@NonNull Archive archive) {
        return dbHelper.findArchiveByName(archive);
    }

    @Override
    public void insertArchives(@NonNull final Archive archive) {
        dbHelper.insertArchives(archive);
    }

    @NonNull
    @Override
    public void deleteArchives(@NonNull  Archive archive) {
        dbHelper.deleteArchives(archive);
    }

    @NonNull
    @Override
    public void updateArchives(@NonNull Archive archive) {
        dbHelper.updateArchives(archive);
    }

    @NonNull
    @Override
    public Flowable<Archive> getArchive() {
        return dbHelper.getArchive();
    }

    @NonNull
    @Override
    public Flowable<Endpoint> getEndpoints(String url) {
        return endpoints.getEndpoints(url);
    }

    @NonNull
    @Override
    public DatabaseReference getDataBaseRefChild_User() {
        return firebase.getDataBaseRefChild_User();
    }

    @NonNull
    @Override
    public DatabaseReference getDataBaseRefChild_Profile() {
        return firebase.getDataBaseRefChild_Profile();
    }

    @NonNull
    @Override
    public FirebaseDatabase getFirebaseDataBase() {
        return firebase.getFirebaseDataBase();
    }

    @NonNull
    @Override
    public Flowable<List<LocalCache>> getAllEndpoints() {
        return dbHelper.getAllEndpoints();
    }

    @NonNull
    @Override
    public Single<LocalCache> findEndpointsByName(@NonNull LocalCache localCache) {
        return dbHelper.findEndpointsByName(localCache);
    }

    @NonNull
    @Override
    public void insertEndpoints(@NonNull LocalCache localCache) {
        dbHelper.insertEndpoints(localCache);
    }

    @NonNull
    @Override
    public void insertAllEndpoints(@NonNull List<LocalCache> localCache) {
        dbHelper.insertAllEndpoints(localCache);
    }

    @NonNull
    @Override
    public void deleteEndpoints(@NonNull LocalCache localCache) {
        dbHelper.deleteEndpoints(localCache);
    }

    @NonNull
    @Override
    public void updateEndpoints(@NonNull LocalCache localCache) {
        dbHelper.updateEndpoints(localCache);
    }

    @NonNull
    @Override
    public Flowable<LocalCache> getEndpoint() {
        return dbHelper.getEndpoint();
    }

    @NonNull
    @Override
    public String getHashMapArchiveIndicator() {
        return preferencesHelper.getHashMapArchiveIndicator();
    }

    @NonNull
    @Override
    public void setHashMapArchiveIndicator(@NonNull String string) {
        preferencesHelper.setHashMapArchiveIndicator(string);
    }

    @NonNull
    @Override
    public void clearHashmapIndicator() {
        preferencesHelper.clearHashmapIndicator();
    }

    @NonNull
    @Override
    public void setInterstitialAd(@NonNull String id) {
        adsRequest.setInterstitialAd(id);
    }

    @NonNull
    @Override
    public void setOnInterstitialInstantiated(@NonNull OnAdsInstantiated onAdsInstantiated) {
        adsRequest.setOnInterstitialInstantiated(onAdsInstantiated);
    }

    @NonNull
    @Override
    public InterstitialAd getInterstitialAd() {
        return adsRequest.getInterstitialAd();
    }

}
