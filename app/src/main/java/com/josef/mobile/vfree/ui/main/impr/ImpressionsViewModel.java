package com.josef.mobile.vfree.ui.main.impr;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.impr.model.Impression;
import com.josef.mobile.vfree.ui.main.impr.remote.DownloadImpressionEndoints;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public final class ImpressionsViewModel extends BaseViewModel {

    @NonNull
    private final DownloadImpressionEndoints downloadImpressionEndoints;

    @Nullable
    private MediatorLiveData<Resource<List<Impression>>> containers;

    @Inject
    public ImpressionsViewModel(@NonNull DownloadImpressionEndoints downloadImpressionEndoints){
        this.downloadImpressionEndoints = downloadImpressionEndoints;
    }

    @NonNull
    public LiveData<Resource<List<Impression>>> observeEndpoints() {
        if (containers == null) containers = new MediatorLiveData<>();
        containers.setValue(Resource.loading(null));

        LiveData<Resource<List<Impression>>> source =
                LiveDataReactiveStreams.fromPublisher(downloadImpressionEndoints.getEndpoints(AppConstants.ENDPOINT_5));

        containers.setValue(Resource.loading(null));
        containers.addSource(source, new Observer<Resource<List<Impression>>>() {
            @Override
            public void onChanged(Resource<List<Impression>> userAuthResource) {
                containers.setValue(userAuthResource);
                containers.removeSource(source);
            }
        });

        return containers;
    }
}
