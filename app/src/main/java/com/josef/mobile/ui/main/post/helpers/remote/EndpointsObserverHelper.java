package com.josef.mobile.ui.main.post.helpers.remote;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.utils.UtilManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.utils.AppConstants.BASE_URL3;

@Singleton
public class EndpointsObserverHelper implements EndpointsObserver {

    private static final String TAG = "EndpointsObserverHelper";

    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final MediatorLiveData<Resource<List<Container>>> containers = new MediatorLiveData<>();


    @Inject
    public EndpointsObserverHelper(DataManager dataManager, UtilManager utilManager) {
        this.dataManager = dataManager;
        this.utilManager = utilManager;
    }


    public Flowable<Resource<List<Container>>> getEndpoints() {

        return dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1")
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<Container>>() {
                    }.getType();
                    ArrayList<Container> endpoints = gson.fromJson(endpoint.message, userListType);
                    return endpoints;
                })

                .onErrorReturn(new Function<Throwable, ArrayList<Container>>() {
                    @Override
                    public ArrayList<Container> apply(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: " + throwable.toString());
                        Container container = new Container();
                        container.setId(-1);
                        ArrayList<Container> containers = new ArrayList<>();
                        containers.add(container);
                        return containers;
                    }
                })

                .map((Function<List<Container>, Resource<List<Container>>>) posts -> {
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
