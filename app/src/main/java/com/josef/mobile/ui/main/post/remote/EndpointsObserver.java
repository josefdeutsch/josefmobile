package com.josef.mobile.ui.main.post.remote;

import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.main.Resource;

import java.util.List;

import io.reactivex.Flowable;

public interface EndpointsObserver {

     Flowable<Resource<List<LocalCache>>> getEndpoints(String index);
}
