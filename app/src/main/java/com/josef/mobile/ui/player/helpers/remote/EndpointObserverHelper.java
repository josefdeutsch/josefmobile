package com.josef.mobile.ui.player.helpers.remote;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.utils.AppConstants.BASE_URL3;

@Singleton
public class EndpointObserverHelper implements EndpointObserver {


    private static final String TAG = "EndpointObserverHelper";

    final DataManager dataManager;


    @Inject
    public EndpointObserverHelper(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Flowable<Resource<Container>> observeEndpoints(int index) {

        Flowable<Integer> value = getIntegerFlowable(index);

        Flowable<ArrayList<Container>> list = getFlowableList();

        return Flowable.zip(value, list, (integer, containers) -> containers.get(integer))
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.toString());
                    Container container = new Container();
                    container.setId(-1);
                    return container;
                })
                .map(container -> {
                    if (container.getId() == -1) {
                        return Resource.error("Something went wrong", null);
                    }
                    return Resource.success(container);
                });

    }

    @NotNull
    private Flowable<Integer> getIntegerFlowable(int index) {
        return Flowable.just(index);
    }

    private Flowable<ArrayList<Container>> getFlowableList() {
        return dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1")
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<Container>>() {
                    }.getType();
                    ArrayList<Container> source = gson.fromJson(endpoint.message, userListType);
                    return source;
                });
    }


}
