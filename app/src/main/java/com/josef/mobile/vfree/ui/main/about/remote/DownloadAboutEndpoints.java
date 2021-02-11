package com.josef.mobile.vfree.ui.main.about.remote;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public interface DownloadAboutEndpoints {
    @NonNull
    Flowable<Resource<List<About>>> getEndpoints(@NonNull String index);
}
