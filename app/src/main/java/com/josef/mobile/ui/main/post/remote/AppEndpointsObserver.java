package com.josef.mobile.ui.main.post.remote;

import android.content.Context;

import com.josef.mobile.R;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.main.Resource;

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
                    ArrayList<LocalCache> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })
                .map((Function<List<LocalCache>, Resource<List<LocalCache>>>) posts -> {
                    if (posts.size() > 0) {
                        if (posts.get(0).getId() == -1) {
                            return Resource.error(context.getResources().getString(R.string.resource_onerror_remainder), null);
                        }
                    }
                    return Resource.success(posts);
                })
                .subscribeOn(Schedulers.io());
    }
}
