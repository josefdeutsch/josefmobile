package com.josef.josefmobile.ui.main.post.remote;

import android.content.Context;

import com.josef.josefmobile.data.DataManager;
import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppEndpointsObserver implements EndpointsObserver {

    private final DataManager dataManager;
    private final Context context;

    @Inject
    public AppEndpointsObserver(Context context, DataManager dataManager) {
        this.dataManager = dataManager;
        this.context = context;
    }

    public Flowable<Resource<List<LocalCache>>> getEndpoints(String index) {

        return dataManager.getAllEndpoints()
                .onErrorReturn((Function<Throwable, ArrayList<LocalCache>>) throwable -> {
                    LocalCache container = new LocalCache();
                    container.setId(-1l);
                    container.setException(throwable.getMessage());
                    ArrayList<LocalCache> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })
                .map((Function<List<LocalCache>, Resource<List<LocalCache>>>) posts -> {
                    if (posts.size() > 0) {
                        if (posts.get(0).getId() == -1) {
                            return Resource.error(posts.get(0).getException(), null);
                        }
                    }
                    return Resource.success(posts);
                })
                .subscribeOn(Schedulers.io());
    }
}
