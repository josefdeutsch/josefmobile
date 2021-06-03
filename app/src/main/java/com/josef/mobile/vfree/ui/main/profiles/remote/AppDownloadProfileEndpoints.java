package com.josef.mobile.vfree.ui.main.profiles.remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.profiles.model.Profile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL;

public class AppDownloadProfileEndpoints implements DownloadProfileEndpoints {

    @NonNull
    private final DataManager dataManager;



    @Inject
    public AppDownloadProfileEndpoints(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @NonNull
    public Flowable<Resource<List<Profile>>> getEndpoints(@NonNull String index) {

        return dataManager.getEndpoints(BASE_URL + index)
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<Profile>>() {
                    }.getType();
                    ArrayList<Profile> endpoints = gson.fromJson(endpoint.message, userListType);
                    return endpoints;
                })

                .onErrorReturn(throwable -> {
                    Profile container = new Profile();
                    container.setId(-1l);
                    container.setException(throwable.getMessage());

                    ArrayList<Profile> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })

                .map((Function<List<Profile>, Resource<List<Profile>>>) posts -> {
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
