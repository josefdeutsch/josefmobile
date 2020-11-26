package com.josef.mobile.ui.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.splash.remote.DownloadEndpoints;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.utils.AppConstants.ENDPOINT_1;

public class SplashViewModel extends BaseViewModel {

    private final DownloadEndpoints downloadEndpoints;
    private final DataManager dataManager;

    private MediatorLiveData<Resource<List<LocalCache>>> containers;

    @Inject
    public SplashViewModel(DownloadEndpoints downloadEndpoints, DataManager dataManager) {
        this.downloadEndpoints = downloadEndpoints;
        this.dataManager = dataManager;
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
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.insertAllEndpoints(archives))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

}
