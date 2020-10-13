package com.josef.mobile.ui.main.post;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.models.Change;
import com.josef.mobile.net.auth.AuthApi;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class IntroViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    private AuthApi authApi;

    private MediatorLiveData<Resource<Change>> posts;

    @Inject
    public IntroViewModel(AuthApi authApi) {
        this.authApi = authApi;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");
    }

    public LiveData<Resource<Change>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading(null));
            final LiveData<Resource<Change>> source = LiveDataReactiveStreams.fromPublisher(
                    authApi.getPostsFromUser()
                            .onErrorReturn(new Function<Throwable, Change>() {
                                @Override
                                public Change apply(Throwable throwable) throws Exception {
                                    Log.e(TAG, "apply: " + throwable.toString());
                                    Change object = new Change();
                                    // object.setId(-1);
                                    //ArrayList<Change> posts = new ArrayList<>();
                                    //posts.add(object);
                                    return object;
                                }
                            })
                            .map(new Function<Change, Resource<Change>>() {
                                @Override
                                public Resource<Change> apply(Change change) throws Exception {
                                    return Resource.success(change);
                                }
                            })
                            .subscribeOn(Schedulers.io()));

            posts.addSource(source, new Observer<Resource<Change>>() {
                @Override
                public void onChanged(Resource<Change> listResource) {
                    posts.setValue(listResource);
                    posts.removeSource(source);
                }
            });
        }
        return posts;
    }

}


