package com.josef.mobile.ui.player.helpers.remote;

import android.util.Log;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.main.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class EndpointObserverHelper implements EndpointObserver {


    private static final String TAG = "EndpointObserverHelper";

    final DataManager dataManager;


    @Inject
    public EndpointObserverHelper(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Flowable<Resource<LocalCache>> observeEndpoints(int index) {

        Flowable<Integer> value = getIntegerFlowable(index);

        Flowable<List<LocalCache>> list = getFlowableList();

        return Flowable.zip(value, list, (integer, containers) -> {
            return containers.get(integer);
        })
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.toString());
                    LocalCache container = new LocalCache();
                    container.setId(-1l);
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

    private Flowable<List<LocalCache>> getFlowableList() {
        return dataManager.getAllEndpoints();
    }


}
