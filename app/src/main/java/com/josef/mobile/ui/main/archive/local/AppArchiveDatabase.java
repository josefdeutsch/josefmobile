package com.josef.mobile.ui.main.archive.local;

import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppArchiveDatabase implements ArchiveDatabase {

    private final DataManager dataManager;

    @Inject
    public AppArchiveDatabase(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public Flowable<Resource<List<Archive>>> getAllArchives() {
        return dataManager.getAllArchives()
                .onErrorReturn(throwable -> {
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
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void deleteArchives(Archive archive) {
        dataManager.deleteArchives(archive);
    }

    @Override
    public void updateEndpoints(Archive archive) {
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
