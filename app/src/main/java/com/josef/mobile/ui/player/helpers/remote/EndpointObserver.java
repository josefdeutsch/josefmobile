package com.josef.mobile.ui.player.helpers.remote;

import androidx.lifecycle.LiveData;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

public interface EndpointObserver {

    LiveData<Resource<Container>> observeEndpoints(final int index);

    LiveData<Resource<Container>> observeContainer();
}
