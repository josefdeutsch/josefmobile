package com.josef.mobile.vfree.ui.main.post.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL3;

@Singleton
public class AppDownloadEndpoints implements DownloadEndpoints {

    private static final String TAG = "EndpointsObserverHelper";

    private final DataManager dataManager;

    @Inject
    public AppDownloadEndpoints(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Flowable<Resource<List<LocalCache>>> getEndpoints(String url) {

        return dataManager.getEndpoints(BASE_URL3 + url)
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<LocalCache>>() {
                    }.getType();
                    ArrayList<LocalCache> endpoints = gson.fromJson(endpoint.message, userListType);
                    return endpoints;
                })

                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.getMessage());
                    LocalCache container = new LocalCache();
                    container.setId(-1l);
                    container.setException(throwable.getMessage());

                    ArrayList<LocalCache> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })

                .map((Function<List<LocalCache>, Resource<List<LocalCache>>>) posts -> {
                    if (posts.size() > 0) {
                        if (posts.get(0).getId() == -1l) {
                            return Resource.error(posts.get(0).getException(), null);
                        }
                    }
                    return Resource.success(posts);
                })

                .subscribeOn(Schedulers.io());

    }
}
