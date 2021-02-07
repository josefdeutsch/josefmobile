package com.josef.mobile.vfree.ui.player.remote;

import android.util.Log;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.utils.AppConstants;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL3;

@Singleton
public class AppDownloadPlayerEndpoints implements DownloadPlayerEndpoints {

    private final DataManager dataManager;

    @Inject
    public AppDownloadPlayerEndpoints(@NotNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Flowable<Resource<LocalCache>> observeEndpoints(@NotNull int index) {

        Flowable<Integer> value = getIntegerFlowable(index);

        Flowable<ArrayList<LocalCache>> list = getFlowableList();

        return Flowable.zip(value, list, (integer, containers) -> containers.get(integer))
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
    private Flowable<Integer> getIntegerFlowable(@NotNull int index) {
        return Flowable.just(index);
    }

    @NotNull
    private Flowable<ArrayList<LocalCache>> getFlowableList() {
        return dataManager.getChange(BASE_URL3 + AppConstants.ENDPOINT_1)
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<LocalCache>>() {
                    }.getType();
                    ArrayList<LocalCache> source = gson.fromJson(endpoint.message, userListType);
                    return source;
                });
    }

}
