package com.josef.mobile.vfree.ui.main.about;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;
import com.josef.mobile.vfree.ui.main.about.remote.DownloadAboutEndpoints;
import com.josef.mobile.vfree.utils.AppConstants;
import com.josef.mobile.vfree.utils.UtilManager;

import java.util.List;

import javax.inject.Inject;

public class AboutViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final DownloadAboutEndpoints endpointsObserver;

    public MediatorLiveData<Resource<List<About>>> getContainers() {
        return containers;
    }

    private MediatorLiveData<Resource<List<About>>> containers;

    @Inject
    public AboutViewModel(DataManager dataManager,
                          DownloadAboutEndpoints endpointsObserver,
                          UtilManager utilManager)
    {

        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.utilManager = utilManager;

    }


    public LiveData<Resource<List<About>>> observeEndpoints(@NonNull String url)
    {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<About>>> source =
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