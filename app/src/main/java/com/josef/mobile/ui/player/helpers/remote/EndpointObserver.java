package com.josef.mobile.ui.player.helpers.remote;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import io.reactivex.Flowable;

public interface EndpointObserver {

     Flowable<Resource<Container>> observeEndpoints(int index);

}
