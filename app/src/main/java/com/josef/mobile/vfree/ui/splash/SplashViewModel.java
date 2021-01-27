package com.josef.mobile.vfree.ui.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.archive.ads.InterstitialAdsRequest;
import com.josef.mobile.vfree.ui.main.archive.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.ui.splash.remote.DownloadEndpoints;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.vfree.utils.AppConstants.ENDPOINT_1;

public class SplashViewModel extends BaseViewModel {

    private final DownloadEndpoints downloadEndpoints;
    private final DataManager dataManager;
    private final InterstitialAdsRequest interstitialAdsRequest;

    private MediatorLiveData<Resource<List<LocalCache>>> containers;

    @Inject
    public SplashViewModel(DownloadEndpoints downloadEndpoints,
                           DataManager dataManager,
                           InterstitialAdsRequest interstitialAdsRequest
    ) {
        this.downloadEndpoints = downloadEndpoints;
        this.dataManager = dataManager;
        this.interstitialAdsRequest = interstitialAdsRequest;
    }

    public LiveData<Resource<List<LocalCache>>> observeEndpoints() {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<LocalCache>>> source =
                LiveDataReactiveStreams.fromPublisher(downloadEndpoints.getEndpoints(ENDPOINT_1));

        containers.setValue(Resource.loading(null));
        containers.addSource(source, new Observer<Resource<List<LocalCache>>>() {
            @Override
            public void onChanged(Resource<List<LocalCache>> userAuthResource) {
                containers.setValue(userAuthResource);
                containers.removeSource(source);
            }
        });

        return containers;
    }

    public void insertAllEndoints(final List<LocalCache> archives) {
       addToCompositeDisposable(
               Completable.fromAction(() -> dataManager.insertAllEndpoints(archives))
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe());
    }

    public void initiateInsterstitialAds() {
        addToCompositeDisposable(
                Completable.fromAction(() ->
                        interstitialAdsRequest.execute())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }
}
