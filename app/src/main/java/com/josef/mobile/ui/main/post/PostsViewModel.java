package com.josef.mobile.ui.main.post;


import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;
import com.josef.mobile.ui.main.post.helpers.remote.EndpointsObserver;
import com.josef.mobile.ui.main.post.model.Container;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends BaseViewModel {

    private static final String TAG = "PostsViewModel";

    private final DataManager dataManager;
    private final EndpointsObserver endpointsObserver;
    private final Context context;

    @Inject
    public PostsViewModel(DataManager dataManager, EndpointsObserver endpointsObserver, Context context) {
        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.context = context;
    }

    public LiveData<Resource<List<Container>>> observeEndpoints() {
        return endpointsObserver.observeEndpoints();
    }


    public void insertArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.insertArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Toast.makeText(context, "Completed!", Toast.LENGTH_SHORT).show(),
                                throwable -> Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()));
    }

    public void deleteArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show(),
                                throwable -> Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()));
    }

}


