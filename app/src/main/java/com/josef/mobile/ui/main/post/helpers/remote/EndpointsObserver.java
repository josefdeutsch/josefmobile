package com.josef.mobile.ui.main.post.helpers.remote;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import java.util.List;

import io.reactivex.Flowable;

public interface EndpointsObserver {

     Flowable<Resource<List<Container>>> getEndpoints(String index);
}
