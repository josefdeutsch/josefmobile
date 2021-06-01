package com.josef.mobile.vfree.ui.main.post;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.post.remote.DownloadEndpoints;
import com.josef.mobile.vfree.utils.AppConstants;
import com.josef.mobile.vfree.utils.UtilManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class PostsViewModel extends BaseViewModel {

    @NonNull
    private final DataManager dataManager;
    @NonNull
    private final UtilManager utilManager;
    @NonNull
    private final DownloadEndpoints endpointsObserver;
    @Nullable
    private MediatorLiveData<Resource<List<LocalCache>>> containers;

    @Inject
    public PostsViewModel(@NonNull DataManager dataManager,
                          @NonNull DownloadEndpoints endpointsObserver,
                          @NonNull UtilManager utilManager,
                          @NonNull Context context) {

        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.utilManager = utilManager;

    }

    @NonNull
    public LiveData<Resource<List<LocalCache>>> observeEndpoints() {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<LocalCache>>> source =
                LiveDataReactiveStreams.fromPublisher(endpointsObserver.getEndpoints(AppConstants.ENDPOINT_1));

        containers.setValue(Resource.loading(null));
        containers.addSource(source, userAuthResource -> {
            containers.setValue(userAuthResource);
            containers.removeSource(source);
        });

        return containers;
    }


    public void insertArchives(@NonNull final Archive archive) {
        addToCompositeDisposable(
                Completable.fromAction(() -> dataManager.insertArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void deleteArchives(@NonNull final Archive archive) {
        addToCompositeDisposable(
                Completable.fromAction(() -> dataManager.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void updateEndpoints(@NonNull final LocalCache localCache) {
        addToCompositeDisposable(
                Completable.fromAction(() -> dataManager.updateEndpoints(localCache))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

    }

    public void initiateInsterstitialAds(@NonNull Activity activity) {
        addToCompositeDisposable(
                Completable.fromAction(() -> {

                    if (dataManager.getInterstitialAd() != null) {
                        dataManager.getInterstitialAd().show(activity);
                    } else {
                      //  Toast.makeText(activity, "Ad did not load", Toast.LENGTH_SHORT).show();
                    }
                })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }
}



