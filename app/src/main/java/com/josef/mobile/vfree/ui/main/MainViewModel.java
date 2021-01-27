package com.josef.mobile.vfree.ui.main;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.josef.mobile.vfree.SessionManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.archive.ads.InterstitialAdsRequest;
import com.josef.mobile.vfree.ui.main.archive.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.ui.main.store.Credentials;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;


public class MainViewModel extends BaseViewModel {

    private final InterstitialAdsRequest interstitialAdsRequest;
    private final Credentials credentials;
    private final Context context;
    private final MediatorLiveData<Resource<Void>> adsRequest = new MediatorLiveData<>();

    public MediatorLiveData<Resource<User>> getDataStoreCredentials() {
        return dataStoreCredentials;
    }

    private final MediatorLiveData<Resource<User>> dataStoreCredentials = new MediatorLiveData<>();

    @Inject
    public MainViewModel(
            InterstitialAdsRequest interstitialAdsRequest,
            Credentials credentials,
            Context context
    ) {

        this.interstitialAdsRequest = interstitialAdsRequest;
        this.credentials = credentials;
        this.context = context;

    }

    public MediatorLiveData<Resource<Void>> observeAdsRequest() {
        return adsRequest;
    }

    private static final String TAG = "MainViewModel";
    public void initiateInsterstitialAds(OnAdsInstantiated onAdsInstantiated) {
        addToCompositeDisposable(
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "run: ");
                        interstitialAdsRequest.setAdListener(onAdsInstantiated);
                    }
                })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }


    public MediatorLiveData<Resource<User>> observeDataStoreCredentials(MainActivity mainActivity) {

        LiveData<Resource<User>> source =
                LiveDataReactiveStreams.fromPublisher(credentials.observeDataStore(mainActivity));

        dataStoreCredentials.setValue(Resource.loading(null));

        dataStoreCredentials.addSource(source, listResource -> {
            dataStoreCredentials.setValue(listResource);
            dataStoreCredentials.removeSource(source);
        });
        return dataStoreCredentials;

    }








}
