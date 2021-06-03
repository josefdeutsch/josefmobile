package com.josef.mobile.vfree.ui.main.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Home;
import com.josef.mobile.vfree.ui.main.home.res.DownloadHomeEndpoints;

import java.util.List;

import javax.inject.Inject;

public final class HomeViewModel extends ViewModel {

    @NonNull
    private final DownloadHomeEndpoints downloadHomeEndpoints;
    @NonNull
    private MediatorLiveData<Resource<List<Home>>> containers;

    @Inject
    public HomeViewModel(@NonNull DownloadHomeEndpoints downloadHomeEndpoints) {
        this.downloadHomeEndpoints = downloadHomeEndpoints;
    }
    @NonNull
    public LiveData<Resource<List<Home>>> observeProfiles(@NonNull String url) {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<Home>>> source =
                LiveDataReactiveStreams.fromPublisher(downloadHomeEndpoints.observeEndpoints(url));

        containers.setValue(Resource.loading(null));
        containers.addSource(source, profileResource -> {
            containers.setValue(profileResource);
            containers.removeSource(source);
        });

        return containers;
    }

}
