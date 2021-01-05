package com.josef.mobile.vfree.ui.splash.remote;

import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import java.util.List;

import io.reactivex.Flowable;

public interface DownloadEndpoints {

     Flowable<Resource<List<LocalCache>>> getEndpoints(String index);
}
