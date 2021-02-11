package com.josef.mobile.vfree.ui.main.archive.local;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class AppArchiveDatabase implements ArchiveDatabase {

    @NonNull
    private final DataManager dataManager;

    @Inject
    public AppArchiveDatabase(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @NonNull
    public Flowable<Resource<List<Archive>>> getAllArchives() {
        return dataManager.getAllArchives()
                .onErrorReturn(throwable -> {
                    Archive archive = new Archive();
                    archive.id = -1l;
                    archive.setException(throwable.getMessage());
                    List<Archive> archives = new ArrayList<>();
                    archives.add(archive);
                    return archives;
                })
                .map((Function<List<Archive>, Resource<List<Archive>>>) archives -> {
                    if (archives.size() > 0) {
                        if (archives.get(0).id == -1l) {
                            return Resource.error(archives.get(0).getException(), null);
                        }
                    }
                    return Resource.success(archives);
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void deleteArchives(@NonNull Archive archive) {
        dataManager.deleteArchives(archive);
    }

    @Override
    public void updateEndpoints(@NonNull Archive archive) {
        archive.setFlag(false);

        LocalCache localCache = new LocalCache(
                archive.getId(),
                archive.isFlag(),
                archive.getName(),
                archive.getUrl(),
                archive.getTag(),
                archive.getPng()
        );

        dataManager.updateEndpoints(localCache);
    }
}
