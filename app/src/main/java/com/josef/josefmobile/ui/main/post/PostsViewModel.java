package com.josef.josefmobile.ui.main.post;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.josefmobile.data.DataManager;
import com.josef.josefmobile.data.local.db.model.Archive;
import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.ui.base.BaseViewModel;
import com.josef.josefmobile.ui.main.Resource;
import com.josef.josefmobile.ui.main.post.remote.EndpointsObserver;
import com.josef.josefmobile.utils.AppConstants;
import com.josef.josefmobile.utils.UtilManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends BaseViewModel {


    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final EndpointsObserver endpointsObserver;

    private MediatorLiveData<Resource<List<LocalCache>>> containers;

    @Inject
    public PostsViewModel(DataManager dataManager,
                          EndpointsObserver endpointsObserver,
                          UtilManager utilManager,
                          Context context) {

        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.utilManager = utilManager;

    }


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


    public void insertArchives(final Archive archive) {
        addToCompositeDisposable(
                Completable.fromAction(() -> dataManager.insertArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void deleteArchives(final Archive archive) {
        addToCompositeDisposable(
                Completable.fromAction(() -> dataManager.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void updateEndpoints(final LocalCache localCache) {
        addToCompositeDisposable(
                Completable.fromAction(() -> dataManager.updateEndpoints(localCache))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

}



