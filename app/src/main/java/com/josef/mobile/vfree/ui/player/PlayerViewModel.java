package com.josef.mobile.vfree.ui.player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.player.remote.DownloadPlayerEndpoints;

import javax.inject.Inject;


public class PlayerViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private final DownloadPlayerEndpoints endpointObserver;

    @Nullable
    private MediatorLiveData<Resource<LocalCache>> container;

    @Inject
    public PlayerViewModel(@NotNull DataManager dataManager, @NotNull DownloadPlayerEndpoints endpointObserver) {
        this.dataManager = dataManager;
        this.endpointObserver = endpointObserver;
    }

    public void authenticateWithEndpoint(@NotNull int index) {
        if (container == null) container = new MediatorLiveData<>();
        container.setValue(Resource.loading(null));
        final LiveData<Resource<LocalCache>> source
                = LiveDataReactiveStreams.fromPublisher(endpointObserver.observeEndpoints(index));
        container.setValue(Resource.loading(null));
        container.addSource(source, new Observer<Resource<LocalCache>>() {
            @Override
            public void onChanged(Resource<LocalCache> userAuthResource) {
                container.setValue(userAuthResource);
                container.removeSource(source);
            }
        });
    }

    @Nullable
    public LiveData<Resource<LocalCache>> observeEndpoints() {
        return container;
    }
}
