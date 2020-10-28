package com.josef.mobile.ui.player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.utils.AppConstants.BASE_URL3;

public class PlayerViewModel extends BaseViewModel {

    private final DataManager dataManager;

    @Inject
    public PlayerViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void authenticateWithEndpoint(final int index) {
        addToContainer(observeEndpoints(index));

    }

    public LiveData<Resource<Container>> observeEndpoints(final int index) {

        return LiveDataReactiveStreams.fromPublisher(
                dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1")
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
    }
}
