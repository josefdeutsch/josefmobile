package com.josef.mobile.vfree.ui.main.home.res;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Profile;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;

import java.util.List;

import io.reactivex.Flowable;

public interface DownloadProfileEndpoints {

     Flowable<Resource<List<Profile>>> observeEndpoints(@NonNull String url);

}
