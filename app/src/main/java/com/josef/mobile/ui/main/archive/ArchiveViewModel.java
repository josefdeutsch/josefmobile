package com.josef.mobile.ui.main.archive;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ArchiveViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    private final DataManager dataManager;

    private MediatorLiveData<Resource<List<Archive>>> posts;

    @Inject
    public ArchiveViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LiveData<Resource<List<Archive>>> observeArchive() {
        if (posts == null) posts = new MediatorLiveData<>();

        posts.setValue(Resource.loading(null));
        final LiveData<Resource<List<Archive>>> source = LiveDataReactiveStreams.fromPublisher(
                dataManager.getAllArchives()
                        .onErrorReturn(throwable -> {
                            Log.e(TAG, "apply: " + throwable.toString());
                            Archive archive = new Archive();
                            archive.id = -1l;
                            List<Archive> archives = new ArrayList<>();
                            archives.add(archive);
                            return archives;
                        })
                        .map((Function<List<Archive>, Resource<List<Archive>>>) archives -> {

                            if (archives.size() > 0) {
                                if (archives.get(0).id == -1l) {
                                    return Resource.error("Error!", null);
                                }
                            }

                            return Resource.success(archives);
                        })
                        .subscribeOn(Schedulers.io()));

        posts.addSource(source, listResource -> {
            posts.setValue(listResource);
            posts.removeSource(source);
        });

        return posts;
    }

    public void deleteArchives(final Archive archive) {
        dataManager.deleteArchives(archive);
        observeArchive();
    }

}


