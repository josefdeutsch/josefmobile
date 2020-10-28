package com.josef.mobile.ui.main.post;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.utils.AppConstants.BASE_URL3;

public class PostsViewModel extends BaseViewModel {

    private static final String TAG = "PostsViewModel";

    private final DataManager dataManager;


    @Inject
    public PostsViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");
        addToListOfContainer(observeEndpoints());
    }

    public LiveData<Resource<List<Container>>> observeEndpoints() {

        return LiveDataReactiveStreams.fromPublisher(
                dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1")

                        .map(endpoint -> {
                            Gson gson = new Gson();
                            Type userListType = new TypeToken<ArrayList<Container>>() {
                            }.getType();
                            ArrayList<Container> endpoints = gson.fromJson(endpoint.message, userListType);
                            return endpoints;
                        })

                        .onErrorReturn(throwable -> {
                            Log.e(TAG, "apply: " + throwable.toString());
                            Container container = new Container();
                            container.setId(-1);
                            ArrayList<Container> containers = new ArrayList<>();
                            containers.add(container);
                            return containers;
                        })

                        .map((Function<List<Container>, Resource<List<Container>>>) posts -> {
                            if (posts.size() > 0) {
                                if (posts.get(0).getId() == -1) {
                                    return Resource.error("Something went wrong", null);
                                }
                            }
                            return Resource.success(posts);
                        })

                        .subscribeOn(Schedulers.io()));
    }

    public void insertArchives(final Archive archive) {
        dataManager.insertArchives(archive);
    }

    public void deleteArchives(final Archive archive) {
        dataManager.deleteArchives(archive);
    }

}


