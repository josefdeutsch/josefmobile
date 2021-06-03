package com.josef.mobile.vfree.ui.main.profiles.remote;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;
import com.josef.mobile.vfree.ui.main.profiles.model.Profile;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public interface DownloadProfileEndpoints {

    @NonNull
    Flowable<Resource<List<Profile>>> getEndpoints(@NonNull String index);
}
