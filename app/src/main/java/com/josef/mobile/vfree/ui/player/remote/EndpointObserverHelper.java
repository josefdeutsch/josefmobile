package com.josef.mobile.vfree.ui.player.remote;

import android.content.Context;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class EndpointObserverHelper implements EndpointObserver {

    private final Context context;

    private final DataManager dataManager;

    @Inject
    public EndpointObserverHelper(Context context, DataManager dataManager) {
        this.dataManager = dataManager;
        this.context = context;
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
                    LocalCache container = new LocalCache();
                    container.setId(-1l);
                    container.setException(throwable.getMessage());
                    return container;
                })
                .map(container -> {
                    if (container.getId() == -1) {
                        return Resource.error(container.getException(), null);
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
