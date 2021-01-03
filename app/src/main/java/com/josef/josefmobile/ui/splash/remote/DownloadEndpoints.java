package com.josef.josefmobile.ui.splash.remote;

import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.ui.main.Resource;

import java.util.List;

import io.reactivex.Flowable;

public interface DownloadEndpoints {

     Flowable<Resource<List<LocalCache>>> getEndpoints(String index);
}
