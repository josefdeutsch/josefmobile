package com.josef.mobile.ui.main.post;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;
import com.josef.mobile.ui.main.post.helpers.remote.EndpointsObserver;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.utils.UtilManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends BaseViewModel {


    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final EndpointsObserver endpointsObserver;
    private final Context context;

    private MediatorLiveData<Resource<List<Container>>> containers;

    @Inject
    public PostsViewModel(DataManager dataManager,
                          EndpointsObserver endpointsObserver,
                          UtilManager utilManager,
                          Context context) {

        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.utilManager = utilManager;
        this.context = context;


    }


    public LiveData<Resource<List<Container>>> observeEndpoints() {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<Container>>> source =
                LiveDataReactiveStreams.fromPublisher(endpointsObserver.getEndpoints());

        containers.setValue(Resource.loading(null));
        containers.addSource(source, userAuthResource -> {
            containers.setValue(userAuthResource);
            containers.removeSource(source);
        });

        return containers;
    }


    public void insertArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.insertArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void deleteArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

}


