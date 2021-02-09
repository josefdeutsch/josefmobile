package com.josef.mobile.vfree.ui.main.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Profile;
import com.josef.mobile.vfree.ui.main.home.res.DownloadProfileEndpoints;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private final DownloadProfileEndpoints downloadProfileEndpoints;
    private MediatorLiveData<Resource<List<Profile>>> containers;

    @Inject
    public HomeViewModel(DownloadProfileEndpoints downloadProfileEndpoints) {
        this.downloadProfileEndpoints = downloadProfileEndpoints;
    }

    public LiveData<Resource<List<Profile>>> observeProfiles(@NonNull String url) {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<Profile>>> source =
                LiveDataReactiveStreams.fromPublisher(downloadProfileEndpoints.observeEndpoints(url));

        containers.setValue(Resource.loading(null));
        containers.addSource(source, profileResource -> {
            containers.setValue(profileResource);
            containers.removeSource(source);
        });

        return containers;
    }

}