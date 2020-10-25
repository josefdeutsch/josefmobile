package com.josef.mobile.ui.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import java.util.List;

public abstract class BaseViewModel extends ViewModel {

    private static final String TAG = "BaseViewModel";

    private final MediatorLiveData<Resource<List<Container>>> containers = new MediatorLiveData<>();

    public MediatorLiveData<Resource<List<Container>>> observeResources() {
        return containers;
    }

    public void addToResource(final LiveData<Resource<List<Container>>> source) {
        containers.setValue(Resource.loading(null));
        containers.addSource(source, new Observer<Resource<List<Container>>>() {
            @Override
            public void onChanged(Resource<List<Container>> userAuthResource) {
                containers.setValue(userAuthResource);
                containers.removeSource(source);
            }
        });

    }

}
