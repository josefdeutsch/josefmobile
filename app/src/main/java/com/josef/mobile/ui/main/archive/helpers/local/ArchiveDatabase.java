package com.josef.mobile.ui.main.archive.helpers.local;

import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.main.Resource;

import java.util.List;

import io.reactivex.Flowable;

public interface ArchiveDatabase {


    void deleteArchives(final Archive archive);

    void deleteArchivesPref(Archive archive);

    Flowable<Resource<List<Archive>>> getAllArchives();
}
