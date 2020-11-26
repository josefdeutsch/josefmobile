package com.josef.mobile.ui.main.post.helpers.remote;

import android.util.Log;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppEndpointsObserver implements EndpointsObserver {

    private static final String TAG = "EndpointsObserverHelper";

    private final DataManager dataManager;


    @Inject
    public AppEndpointsObserver(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Flowable<Resource<List<LocalCache>>> getEndpoints(String index) {

        return dataManager.getAllEndpoints()

                .onErrorReturn(new Function<Throwable, ArrayList<LocalCache>>() {
                    @Override
                    public ArrayList<LocalCache> apply(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: " + throwable.toString());
                        LocalCache container = new LocalCache();
                        container.setId(-1l);
                        ArrayList<LocalCache> containers = new ArrayList<>();
                        containers.add(container);
                        return containers;
                    }
                })

                .map((Function<List<LocalCache>, Resource<List<LocalCache>>>) posts -> {
                    if (posts.size() > 0) {
                        if (posts.get(0).getId() == -1) {
                            return Resource.error("Something went wrong", null);
                        }
                    }
                    return Resource.success(posts);
                })

                .subscribeOn(Schedulers.io());
    }
}
