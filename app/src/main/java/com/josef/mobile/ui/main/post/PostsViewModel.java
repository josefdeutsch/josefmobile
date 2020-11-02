package com.josef.mobile.ui.main.post;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;
import com.josef.mobile.ui.main.post.helpers.remote.EndpointsObserver;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.utils.UtilManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends BaseViewModel {

    private static final String TAG = "PostsViewModel";

    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final EndpointsObserver endpointsObserver;
    private final Context context;
    private Boolean hasInternet = null;

    @Inject
    public PostsViewModel(DataManager dataManager,
                          EndpointsObserver endpointsObserver,
                          UtilManager utilManager,
                          Context context) {

        this.dataManager = dataManager;
        this.endpointsObserver = endpointsObserver;
        this.utilManager = utilManager;
        this.context = context;

        /**   compositeDisposable.add(
         utilManager.isInternet()
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribeWith(new DisposableSingleObserver<Boolean>() {
        @Override public void onSuccess(@NonNull Boolean aBoolean) {
        Toast.makeText(context, aBoolean.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override public void onError(@NonNull Throwable e) {

        }
        }));**/
    }

    public boolean isHasInternet() {
        return hasInternet;
    }

    public void setHasInternet(boolean hasInternet) {
        this.hasInternet = hasInternet;
    }

    public LiveData<Resource<List<Container>>> observeEndpoints() {
        Log.d(TAG, "observeEndpoints: " + hasInternet);
        return endpointsObserver.observeEndpoints();
    }


    public void insertArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.insertArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    public void deleteArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }


}


