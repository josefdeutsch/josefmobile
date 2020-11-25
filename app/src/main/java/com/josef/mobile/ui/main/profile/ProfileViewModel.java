package com.josef.mobile.ui.main.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.SessionManager;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;


public class ProfileViewModel extends ViewModel {

    private static final String TAG = "DaggerExample";

    private final SessionManager sessionManager;

    @Inject
    public ProfileViewModel(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        Log.d(TAG, "ProfileViewModel: ");
    }

    public LiveData<Resource<String>> observeHeader() {
        // return sessionManager.getAuthUser();
        return null;
    }

    public LiveData<Resource<String>> observeSubHeader() {
        // return sessionManager.getAuthUser();
        return null;
    }

    public LiveData<Resource<String>> observeArticle() {
        // return sessionManager.getAuthUser();
        return null;
    }
}
