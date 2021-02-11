package com.josef.mobile.vfree.ui.main.archive.local;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.Resource;

import java.util.List;

import io.reactivex.Flowable;

public interface ArchiveDatabase {

    void deleteArchives(@NonNull Archive archive);

    void updateEndpoints(@NonNull Archive archive);

    @NonNull
    Flowable<Resource<List<Archive>>> getAllArchives();
}
