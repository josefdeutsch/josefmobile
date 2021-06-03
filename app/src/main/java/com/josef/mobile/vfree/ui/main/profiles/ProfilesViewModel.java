package com.josef.mobile.vfree.ui.main.profiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;
import com.josef.mobile.vfree.ui.main.about.remote.DownloadAboutEndpoints;
import com.josef.mobile.vfree.ui.main.profiles.model.Profile;
import com.josef.mobile.vfree.ui.main.profiles.remote.DownloadProfileEndpoints;
import com.josef.mobile.vfree.utils.UtilManager;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public final class ProfilesViewModel extends BaseViewModel {

    @NonNull
    private final DataManager dataManager;

    @NonNull
    private final UtilManager utilManager;

    @NonNull
    private final DownloadProfileEndpoints endpointsObserver;

    @Nullable
    private MediatorLiveData<Resource<List<Profile>>> containers;


    @Nullable
    public MediatorLiveData<Resource<List<Profile>>> getContainers() {
        return Objects.requireNonNull(containers,
                "com.josef.mobile.vfree.ui.main.about.AboutViewModel " +
                        "MediatorLiveData containers, must not be null" );
    }



    @Inject
    public ProfilesViewModel(@NonNull DataManager dataManager,
                             @NonNull DownloadProfileEndpoints endpointsObserver,
                             @NonNull UtilManager utilManager) {

        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.utilManager = utilManager;

    }

    @NonNull
    public LiveData<Resource<List<Profile>>> observeEndpoints(@NonNull String url) {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<Profile>>> source =
                LiveDataReactiveStreams
                        .fromPublisher(endpointsObserver.getEndpoints(url));

        containers.setValue(Resource.loading(null));
        containers.addSource(source, userAuthResource -> {
            containers.setValue(userAuthResource);
            containers.removeSource(source);
        });

        return containers;
    }

}