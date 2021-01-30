package com.josef.mobile.vfree.ui.main.home.res;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Profile;

import java.util.List;

import io.reactivex.Flowable;

public interface ResourceObserver {

    Flowable<Resource<List<Profile>>> getAllProfiles();

}
