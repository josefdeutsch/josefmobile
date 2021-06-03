package com.josef.mobile.vfree.ui.main.home.res;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Home;

import java.util.List;

import io.reactivex.Flowable;

public interface DownloadHomeEndpoints {

     @NonNull
     Flowable<Resource<List<Home>>> observeEndpoints(@NonNull String url);

}
