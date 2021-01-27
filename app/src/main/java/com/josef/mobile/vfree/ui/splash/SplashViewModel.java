package com.josef.mobile.vfree.ui.splash;

import android.os.Looper;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.base.BaseViewModel;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashViewModel extends BaseViewModel {

    private final DataManager dataManager;

    @Inject
    public SplashViewModel(
                           DataManager dataManager
    ) {
        this.dataManager = dataManager;

    }

    public void initiateInsterstitialAds(String id) {
        addToCompositeDisposable(
                Completable.fromAction(() ->
                        dataManager.setInterstitialAd(id))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }
}
