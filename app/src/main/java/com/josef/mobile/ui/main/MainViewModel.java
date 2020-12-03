package com.josef.mobile.ui.main;


import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.archive.ads.InterstitialAdsRequest;
import com.josef.mobile.ui.main.archive.ads.OnAdsInstantiated;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class MainViewModel extends BaseViewModel {

    private final InterstitialAdsRequest interstitialAdsRequest;
    private final MediatorLiveData<Resource<Void>> adsRequest = new MediatorLiveData<>();

    @Inject
    public MainViewModel(InterstitialAdsRequest interstitialAdsRequest) {
        this.interstitialAdsRequest = interstitialAdsRequest;

    }

    public MediatorLiveData<Resource<Void>> observeAdsRequest() {
        return adsRequest;
    }

    public void initiateInsterstitialAds(OnAdsInstantiated onAdsInstantiated) {
      addToCompositeDisposable(
              Completable.fromAction(() -> interstitialAdsRequest.execute(onAdsInstantiated))
                      .subscribeOn(AndroidSchedulers.mainThread())
                      .subscribe());
    }
}
