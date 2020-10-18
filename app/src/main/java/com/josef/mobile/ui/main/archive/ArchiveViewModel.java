package com.josef.mobile.ui.main.archive;


import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.models.Change;
import com.josef.mobile.net.main.MainApi;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;

public class ArchiveViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    // inject
    private final MainApi mainApi;

    private MediatorLiveData<Resource<Change>> posts;

    @Inject
    public ArchiveViewModel(MainApi mainApi) {
        this.mainApi = mainApi;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");
    }


}


