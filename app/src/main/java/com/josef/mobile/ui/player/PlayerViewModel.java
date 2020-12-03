package com.josef.mobile.ui.player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.player.remote.EndpointObserver;

import javax.inject.Inject;

public class PlayerViewModel extends BaseViewModel {

    private final EndpointObserver endpointObserver;

    private MediatorLiveData<Resource<LocalCache>> container;

    @Inject
    public PlayerViewModel(EndpointObserver endpointObserver) {
        this.endpointObserver = endpointObserver;
    }

    public void authenticateWithEndpoint(final int index) {
        if (container == null) container = new MediatorLiveData<>();
        container.setValue(Resource.loading(null));
        final LiveData<Resource<LocalCache>> source
                = LiveDataReactiveStreams.fromPublisher(endpointObserver.observeEndpoints(index));
        container.setValue(Resource.loading(null));
        container.addSource(source, userAuthResource -> {
            container.setValue(userAuthResource);
            container.removeSource(source);
        });
    }

    public LiveData<Resource<LocalCache>> observeEndpoints() {
        return container;
    }
}
