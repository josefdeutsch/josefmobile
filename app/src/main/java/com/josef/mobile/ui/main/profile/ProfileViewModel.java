package com.josef.mobile.ui.main.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.profile.model.Profile;
import com.josef.mobile.ui.main.profile.res.ResourceObserver;

import java.util.List;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {

    private final ResourceObserver resourceObserver;
    private MediatorLiveData<Resource<List<Profile>>> containers;

    @Inject
    public ProfileViewModel(ResourceObserver resourceObserver) {
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
