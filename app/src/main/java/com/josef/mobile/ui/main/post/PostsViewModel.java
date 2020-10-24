package com.josef.mobile.ui.main.post;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.remote.model.Endpoint;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.josef.mobile.util.Constants.BASE_URL3;

public class PostsViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    private final DataManager dataManager;

    private MediatorLiveData<Resource<Endpoint>> posts;

    @Inject
    public PostsViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");
    }


    public LiveData<Resource<Endpoint>> observePosts() {
        if (posts == null) posts = new MediatorLiveData<>();
        posts.setValue(Resource.loading(null));
        final LiveData<Resource<Endpoint>> source = LiveDataReactiveStreams.fromPublisher(
                dataManager.getChange(BASE_URL3 + "_ah/api/echo/v1/echo?n=1")
                        .onErrorReturn(new Function<Throwable, Endpoint>() {
                            @Override
                            public Endpoint apply(@NonNull Throwable throwable) throws Exception {
                                Log.e(TAG, "apply: " + throwable.toString());
                                Endpoint endpoint = new Endpoint();
                                endpoint.id = -1l;
                                return endpoint;
                            }
                        })

                        .map(new Function<Endpoint, Resource<Endpoint>>() {
                            @Override
                            public Resource<Endpoint> apply(Endpoint endpoint) throws Exception {
                                if (endpoint.id == -1l) {
                                    return Resource.error("Error!", null);
                                }
                                return Resource.success(endpoint);
                            }
                        })
                            .subscribeOn(Schedulers.io()));

        posts.addSource(source, new Observer<Resource<Endpoint>>() {
            @Override
            public void onChanged(Resource<Endpoint> listResource) {
                posts.setValue(listResource);
                posts.removeSource(source);
            }
        });
        return posts;
    }

    public void insertArchives(final Archive archive) {
        dataManager.insertArchives(archive);
    }

}


