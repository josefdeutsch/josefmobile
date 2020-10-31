package com.josef.mobile.ui.main.archive.helpers.local;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;
import com.josef.mobile.utils.CommonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class ArchiveDatabaseHelper implements ArchiveDatabase {

    private static final String TAG = "ArchiveDatabaseHelper";
    private final Context context;
    private final CommonUtils commonUtils;
    private final DataManager dataManager;
    private MediatorLiveData<Resource<List<Archive>>> posts;


    @Inject
    public ArchiveDatabaseHelper(DataManager dataManager, CommonUtils commonUtils, Context context) {
        this.dataManager = dataManager;
        this.commonUtils = commonUtils;
        this.context = context;
    }


    public LiveData<Resource<List<Archive>>> observeArchive() {
        if (posts == null) posts = new MediatorLiveData<>();
        Flowable<List<Archive>> allArchives = dataManager.getAllArchives();
        posts.setValue(Resource.loading(null));
        final LiveData<Resource<List<Archive>>> source = LiveDataReactiveStreams.fromPublisher(
                allArchives
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

    @Override
    public void deleteArchives(Archive archive, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(
                Completable.fromAction(() -> dataManager.deleteArchives(archive))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show(),
                                throwable -> Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()));
        observeArchive();
    }

    @Override
    public void deleteArchivesPref(Archive archive, CompositeDisposable compositeDisposable) {
        long id = archive.id;
        compositeDisposable.add(
                Completable.fromAction(() -> {
                    Type sparseArrayType = new TypeToken<HashMap<Integer, Boolean>>() {
                    }.getType();
                    Gson gson = commonUtils.getGson();
                    String stringmap = dataManager.getHashString();
                    HashMap<Integer, Boolean> map = gson.fromJson(stringmap, sparseArrayType);
                    map.replace((int) id, false);
                    String string = gson.toJson(map);
                    dataManager.setHashString(string);
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
        //.subscribe(() -> Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show(),
        //      throwable -> Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show());
    }
}
