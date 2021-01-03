package com.josef.josefmobile.ui.main.profile.res;

import com.josef.josefmobile.ui.main.Resource;
import com.josef.josefmobile.ui.main.profile.model.Profile;

import java.util.List;

import io.reactivex.Flowable;

public interface ResourceObserver {

    Flowable<Resource<List<Profile>>> getAllProfiles();

}
