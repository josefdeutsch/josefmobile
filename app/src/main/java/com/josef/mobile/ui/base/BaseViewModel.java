package com.josef.mobile.ui.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import java.util.List;

public abstract class BaseViewModel extends ViewModel {

    private static final String TAG = "BaseViewModel";

    private final MediatorLiveData<Resource<List<Container>>> containers = new MediatorLiveData<>();

    private final MediatorLiveData<Resource<Container>> container = new MediatorLiveData<>();

    private final MediatorLiveData<Resource<Archive>> archives = new MediatorLiveData<>();


    public LiveData<Resource<List<Container>>> observeListofContainer() {
        return containers;
    }

    public void addToListOfContainer(final LiveData<Resource<List<Container>>> source) {
        containers.setValue(Resource.loading(null));
        containers.addSource(source, new Observer<Resource<List<Container>>>() {
            @Override
            public void onChanged(Resource<List<Container>> userAuthResource) {
                containers.setValue(userAuthResource);
                containers.removeSource(source);
            }
        });

    }

    public LiveData<Resource<Container>> observeContainer() {
        return container;
    }

    public void addToContainer(final LiveData<Resource<Container>> source) {
        container.setValue(Resource.loading(null));
        container.addSource(source, new Observer<Resource<Container>>() {
            @Override
            public void onChanged(Resource<Container> userAuthResource) {
                container.setValue(userAuthResource);
                container.removeSource(source);
            }
        });

    }

    public LiveData<Resource<Archive>> observeArchives() {
        return archives;
    }


    public void addToArchives(final LiveData<Resource<Archive>> source) {
        archives.setValue(Resource.loading(null));
        archives.addSource(source, new Observer<Resource<Archive>>() {
            @Override
            public void onChanged(Resource<Archive> userAuthResource) {
                archives.setValue(userAuthResource);
                archives.removeSource(source);
            }
        });

    }

}
