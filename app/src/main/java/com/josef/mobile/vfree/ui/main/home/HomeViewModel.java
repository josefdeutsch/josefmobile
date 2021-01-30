package com.josef.mobile.vfree.ui.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Profile;
import com.josef.mobile.vfree.ui.main.home.res.ResourceObserver;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private final ResourceObserver resourceObserver;
    private MediatorLiveData<Resource<List<Profile>>> containers;

    @Inject
    public HomeViewModel(ResourceObserver resourceObserver) {
        this.resourceObserver = resourceObserver;
    }

    public LiveData<Resource<List<Profile>>> observeProfiles() {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<Profile>>> source =
                LiveDataReactiveStreams.fromPublisher(resourceObserver.getAllProfiles());

        containers.setValue(Resource.loading(null));
        containers.addSource(source, profileResource -> {
            containers.setValue(profileResource);
            containers.removeSource(source);
        });

        return containers;
    }

}
