package com.josef.mobile.ui.player.remote;

import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.main.Resource;

import io.reactivex.Flowable;

public interface EndpointObserver {

     Flowable<Resource<LocalCache>> observeEndpoints(int index);

}
