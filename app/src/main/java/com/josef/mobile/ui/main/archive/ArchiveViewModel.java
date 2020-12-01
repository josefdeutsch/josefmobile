package com.josef.mobile.ui.main.archive;


import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.fire.FirebaseUpload;
import com.josef.mobile.ui.main.archive.local.ArchiveDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ArchiveViewModel extends BaseViewModel {

    private final FirebaseUpload firebaseUpload;
    private final ArchiveDatabase archiveDatabase;

    private final Context context;

    private MediatorLiveData<Resource<List<Archive>>> posts;

    @Inject
    public ArchiveViewModel(FirebaseUpload firebaseUpload,
                            ArchiveDatabase archiveDatabase,
                            Context context) {

        this.firebaseUpload = firebaseUpload;
        this.archiveDatabase = archiveDatabase;
        this.context = context;
    }

    public LiveData<Resource<List<Archive>>> observeArchive() {

        if (posts == null) posts = new MediatorLiveData<>();

        LiveData<Resource<List<Archive>>> source =
                LiveDataReactiveStreams.fromPublisher(archiveDatabase.getAllArchives());

        posts.setValue(Resource.loading(null));

        posts.addSource(source, listResource -> {
            posts.setValue(listResource);
            posts.removeSource(source);
        });

        return posts;
    }

    public void synchronize(MainActivity mainActivity) {
        compositeDisposable.add(
                firebaseUpload.synchronize(mainActivity)
                        .subscribeWith(new DisposableObserver<DatabaseReference>() {
                            @Override
                            public void onNext(@NonNull DatabaseReference reference) {
                                Toast.makeText(context, "Success !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                            }
                        }));
    }


    public void deleteArchives(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> archiveDatabase.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
        observeArchive();

    }

    public void updateEndpoints(final Archive archive) {
        compositeDisposable.add(
                Completable.fromAction(() -> archiveDatabase.updateEndpoints(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

    }
}


