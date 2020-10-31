package com.josef.mobile.ui.player.helpers.remote;

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

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.utils.AppConstants.BASE_URL3;

@Singleton
public class EndpointObserverHelper implements EndpointObserver {


    final DataManager dataManager;
    private MediatorLiveData<Resource<Container>> container = new MediatorLiveData<>();

    @Inject
    public EndpointObserverHelper(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LiveData<Resource<Container>> observeContainer() {
        return container;
    }

    public void addToContainer(final LiveData<Resource<Container>> source) {
        container.setValue(Resource.loading(null));
        container.addSource(source, new Observer<Resource<Container>>() {
            @Override
            public void onChanged(Resource<Container> userAuthResource) {
                container.setValue(userAuthResource);
                container.removeSource(source);
            }
        });

    }

    @Override
    public LiveData<Resource<Container>> observeEndpoints(int index) {
        if (container == null) container = new MediatorLiveData<>();
        Flowable<Endpoint> endpointFlowable = dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1");
        container.setValue(Resource.loading(null));
        final LiveData<Resource<Container>> source = LiveDataReactiveStreams.fromPublisher(
                endpointFlowable
                        .map(endpoint -> {
                            Gson gson = new Gson();
                            Type userListType = new TypeToken<ArrayList<Container>>() {
                            }.getType();
                            ArrayList<Container> endpoints = gson.fromJson(endpoint.message, userListType);
                            return endpoints;

                        })
                        .map(endpoints -> endpoints.get(index))
                        .onErrorReturn(throwable -> {
                            Container container = new Container();
                            container.setId(-1);
                            return container;
                        })
                        .map((Function<Container, Resource<Container>>) container -> {
                            if (container.getId() == -1) {
                                return Resource.error("Something went wrong", null);
                            }
                            return Resource.success(container);
                        })
                        .subscribeOn(Schedulers.io()));

        addToContainer(source);

        return container;
    }
}
