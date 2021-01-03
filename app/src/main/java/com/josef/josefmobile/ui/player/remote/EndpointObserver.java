package com.josef.josefmobile.ui.player.remote;

import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.ui.main.Resource;

import io.reactivex.Flowable;

public interface EndpointObserver {

     Flowable<Resource<LocalCache>> observeEndpoints(int index);

}
