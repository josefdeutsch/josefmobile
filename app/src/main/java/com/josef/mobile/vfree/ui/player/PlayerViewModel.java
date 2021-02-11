package com.josef.mobile.vfree.ui.player;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.player.remote.DownloadPlayerEndpoints;

import java.util.Objects;

import javax.inject.Inject;


public final class PlayerViewModel extends BaseViewModel {
    @NonNull
    private final DataManager dataManager;
    @NonNull
    private final DownloadPlayerEndpoints endpointObserver;
    @Nullable
    private MediatorLiveData<Resource<LocalCache>> container;

    @Inject
    public PlayerViewModel(@NotNull DataManager dataManager,
                           @NotNull DownloadPlayerEndpoints endpointObserver) {
        this.dataManager = dataManager;
        this.endpointObserver = endpointObserver;
    }

    @Nullable
    public MediatorLiveData<Resource<LocalCache>>getDataStoreCredentials() {
        return Objects.requireNonNull(container,
                "com.josef.mobile.vfree.ui.player.PlayerViewModel " +
                        "container must not be null" );
    }

    public void authenticateWithEndpoint(@NotNull int index,
                                         @NonNull String endpoint)
    {
        if (container == null) container = new MediatorLiveData<>();
        container.setValue(Resource.loading(null));
        final LiveData<Resource<LocalCache>> source
                = LiveDataReactiveStreams.fromPublisher(endpointObserver.observeEndpoints(index,endpoint));
        container.setValue(Resource.loading(null));
        container.addSource(source, userAuthResource -> {
            container.setValue(userAuthResource);
            container.removeSource(source);
        });
    }

    @Nullable
    public LiveData<Resource<LocalCache>> observeEndpoints() {
        return container;
    }
}
