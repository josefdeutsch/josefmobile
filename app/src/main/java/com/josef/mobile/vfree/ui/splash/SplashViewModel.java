package com.josef.mobile.vfree.ui.splash;

import android.content.Context;
import android.media.Image;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private final Context context;

    @Inject
    public SplashViewModel(
            DataManager dataManager,
            RequestManager requestManager,
            Context context
    ) {
        this.dataManager = dataManager;
        this.context = context;

    }

    public void initiateInsterstitialAds(String id) {
        addToCompositeDisposable(
                Completable.fromAction(() ->
                        dataManager.setInterstitialAd(id))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void supplyAnimatedGif(ImageView view) {



    }

}
