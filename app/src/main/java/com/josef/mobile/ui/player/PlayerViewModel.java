package com.josef.mobile.ui.player;

import androidx.lifecycle.LiveData;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.ui.player.helpers.remote.EndpointObserver;

import javax.inject.Inject;

public class PlayerViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private final EndpointObserver endpointObserver;

    @Inject
    public PlayerViewModel(DataManager dataManager, EndpointObserver endpointObserver) {
        this.dataManager = dataManager;
        this.endpointObserver = endpointObserver;
    }

    public void authenticateWithEndpoint(final int index) {
        endpointObserver.observeEndpoints(index);
    }

    public LiveData<Resource<Container>> observeEndpoints() {
        return endpointObserver.observeContainer();
    }
}
