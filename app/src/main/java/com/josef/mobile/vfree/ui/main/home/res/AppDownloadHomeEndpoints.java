package com.josef.mobile.vfree.ui.main.home.res;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Home;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL;

@Singleton
public final class AppDownloadHomeEndpoints implements DownloadHomeEndpoints {

    @NonNull
    private final Context context;
    @NonNull
    private final DataManager dataManager;

    @Inject
    public AppDownloadHomeEndpoints(@NonNull DataManager dataManager,
                                    @NonNull Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }

    @NonNull
    @Override
    public Flowable<Resource<List<Home>>> observeEndpoints(@NonNull String url) {
        return dataManager.getEndpoints(BASE_URL + url)
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<Home>>() {
                    }.getType();
                    ArrayList<Home> endpoints = gson.fromJson(endpoint.message, userListType);
                    return endpoints;
                })

                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.getMessage());
                    Home container = new Home();
                    container.setId(-1l);
                    container.setException(throwable.getMessage());

                    ArrayList<Home> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })

                .map((Function<List<Home>, Resource<List<Home>>>) posts -> {
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
