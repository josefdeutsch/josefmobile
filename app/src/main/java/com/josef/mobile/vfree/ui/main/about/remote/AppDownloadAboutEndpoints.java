package com.josef.mobile.vfree.ui.main.about.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;

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
public class AppDownloadAboutEndpoints implements DownloadAboutEndpoints {

    private static final String TAG = "EndpointsAboutObserverHelper";

    private final DataManager dataManager;

    @Inject
    public AppDownloadAboutEndpoints(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Flowable<Resource<List<About>>> getEndpoints(String index) {

        return dataManager.getChange(BASE_URL3 + index)
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<About>>() {
                    }.getType();
                    ArrayList<About> endpoints = gson.fromJson(endpoint.message, userListType);
                    return endpoints;
                })

                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.getMessage());
                    About container = new About();
                    container.setId(-1l);
                    container.setException(throwable.getMessage());

                    ArrayList<About> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })

                .map((Function<List<About>, Resource<List<About>>>) posts -> {
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
