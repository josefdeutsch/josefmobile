package com.josef.mobile.vfree.ui.splash;

import android.content.Context;
import android.media.Image;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;

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

import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL;

public final class SplashViewModel extends BaseViewModel {

    @NonNull
    private final DataManager dataManager;
    @NonNull
    private final Context context;

    @Inject
    public SplashViewModel(
            @NonNull DataManager dataManager,
            @NonNull RequestManager requestManager,
            @NonNull Context context
    ) {
        this.dataManager = dataManager;
        this.context = context;

    }

    // public void initiateInsterstitialAds(@NonNull String id) {
    // addToCompositeDisposable(
    //    Completable.fromAction(() ->
    //  dataManager.setInterstitialAd(id))
    //     .subscribeOn(AndroidSchedulers.mainThread())
    //     .subscribe());
    //}

    public void initiateRetrofitClient(@NonNull String url) {
        addToCompositeDisposable(
                Completable.fromAction(() ->
                        dataManager.getEndpoints(BASE_URL + url))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }
}
