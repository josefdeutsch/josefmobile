package com.josef.mobile.vfree.ui.main.archive;


import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.archive.fire.FirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.local.ArchiveDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public final class ArchiveViewModel extends BaseViewModel {

    @NonNull
    private final FirebaseUpload firebaseUpload;
    @NonNull
    private final ArchiveDatabase archiveDatabase;
    @NonNull
    private final Context context;
    @Nullable
    private MediatorLiveData<Resource<List<Archive>>> posts;

    @Inject
    public ArchiveViewModel( @NonNull FirebaseUpload firebaseUpload,
                             @NonNull ArchiveDatabase archiveDatabase,
                             @NonNull Context context) {

        this.firebaseUpload = firebaseUpload;
        this.archiveDatabase = archiveDatabase;
        this.context = context;
    }

    @NonNull
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

    public void synchronize(@NonNull MainActivity mainActivity) {
        addToCompositeDisposable(
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


    public void deleteArchives(@NonNull final Archive archive) {
        addToCompositeDisposable(
                Completable.fromAction(() -> archiveDatabase.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
        observeArchive();

    }

    public void updateEndpoints(@NonNull final Archive archive) {
        addToCompositeDisposable(
                Completable.fromAction(() -> archiveDatabase.updateEndpoints(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

    }
}


