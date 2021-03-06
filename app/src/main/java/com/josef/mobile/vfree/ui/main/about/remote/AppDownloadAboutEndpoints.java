package com.josef.mobile.vfree.ui.main.about.remote;

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
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.vfree.utils.AppConstants.BASE_URL;

@Singleton
public class AppDownloadAboutEndpoints implements DownloadAboutEndpoints {

    @NonNull private final DataManager dataManager;

    @Inject
    public AppDownloadAboutEndpoints(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @NonNull
    public Flowable<Resource<List<About>>> getEndpoints(@NonNull String index) {

        return dataManager.getEndpoints(BASE_URL + index)
                .map(endpoint -> {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<About>>() {
                    }.getType();
                    ArrayList<About> endpoints = gson.fromJson(endpoint.message, userListType);
                    return endpoints;
                })

                .onErrorReturn(throwable -> {
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
