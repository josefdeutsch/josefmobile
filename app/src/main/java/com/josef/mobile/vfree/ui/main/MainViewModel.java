package com.josef.mobile.vfree.ui.main;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.ads.AdsRequest;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.data.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.ui.main.store.Credentials;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;


public class MainViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private final Credentials credentials;

    private static final String TAG = "MainViewModel";

    public MediatorLiveData<Resource<User>> getDataStoreCredentials() {
        return dataStoreCredentials;
    }

    private final MediatorLiveData<Resource<User>> dataStoreCredentials = new MediatorLiveData<>();

    @Inject
    public MainViewModel(
            DataManager dataManager,
            Credentials credentials
    ) {

        this.dataManager = dataManager;
        this.credentials = credentials;

    }


    public void initiateInsterstitialAds(OnAdsInstantiated onAdsInstantiated) {
        addToCompositeDisposable(
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        dataManager.getInterstitialAd().show();
                        dataManager.setOnAdsInstantiated(onAdsInstantiated);

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
