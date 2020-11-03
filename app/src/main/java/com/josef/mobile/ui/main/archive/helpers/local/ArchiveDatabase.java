package com.josef.mobile.ui.main.archive.helpers.local;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;

import java.util.List;

import io.reactivex.Flowable;

public interface ArchiveDatabase {


    void deleteArchives(final Archive archive);

    void deleteArchivesPref(Archive archive);

    Flowable<Resource<List<Archive>>> getAllArchives();
}
