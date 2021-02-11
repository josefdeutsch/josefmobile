package com.josef.mobile.vfree.ui.player.remote;

import android.util.Log;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import static android.content.ContentValues.TAG;
import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL;

@Singleton
public final class AppDownloadPlayerEndpoints implements DownloadPlayerEndpoints {

    private final DataManager dataManager;

    @Inject
    public AppDownloadPlayerEndpoints(
            @NotNull DataManager dataManager)
    {
        this.dataManager = dataManager;
    }

    @Override
    public Flowable<Resource<LocalCache>> observeEndpoints(@NotNull int index,
                                                           @NonNull String endpoint)
    {

        Flowable<Integer> value = getIntegerFlowable(index);

        Flowable<ArrayList<LocalCache>> list = getFlowableList(endpoint);

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
    private Flowable<ArrayList<LocalCache>> getFlowableList(@NonNull String endpoint) {
        return dataManager.getEndpoints(BASE_URL + endpoint)
                .map(endpoints -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<LocalCache>>() {
                    }.getType();
                    ArrayList<LocalCache> source = gson.fromJson(endpoints.message, userListType);
                    return source;
                });
    }

}
