package com.josef.mobile.ui.main.post.helpers.remote;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.remote.model.Endpoint;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

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
    private MediatorLiveData<Resource<List<Container>>> containers = new MediatorLiveData<>();

    @Inject
    public EndpointsObserverHelper(DataManager dataManager) {
        this.dataManager = dataManager;

    }

    public void addToContainers(final LiveData<Resource<List<Container>>> source) {
        containers.setValue(Resource.loading(null));
        containers.addSource(source, new Observer<Resource<List<Container>>>() {
            @Override
            public void onChanged(Resource<List<Container>> userAuthResource) {
                containers.setValue(userAuthResource);
                containers.removeSource(source);
            }
        });

    }

    public LiveData<Resource<List<Container>>> observeEndpoints() {
        if (containers == null) containers = new MediatorLiveData<>();
        Flowable<Endpoint> endpointFlowable = dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1");
        containers.setValue(Resource.loading(null));
        final LiveData<Resource<List<Container>>> source = LiveDataReactiveStreams.fromPublisher(
                endpointFlowable
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

                        .subscribeOn(Schedulers.io()));

        addToContainers(source);

        return containers;
    }
}
