package com.josef.mobile.ui.main.post.helpers.remote;

import androidx.lifecycle.LiveData;

import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;

import java.util.List;

public interface EndpointsObserver {

     LiveData<Resource<List<Container>>> observeEndpoints();
}
