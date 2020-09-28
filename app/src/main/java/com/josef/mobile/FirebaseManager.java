package com.josef.mobile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseUser;
import com.josef.mobile.ui.auth.AuthResource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseManager {

    private static final String TAG = "DaggerExample";
    // data
    private MediatorLiveData<AuthResource<FirebaseUser>> cachedUser = new MediatorLiveData<>();

    @Inject
    public FirebaseManager() {

    }

    public void authenticateWithId(final LiveData<AuthResource<FirebaseUser>> source) {
        if (cachedUser != null) {
            cachedUser.setValue(AuthResource.loading((FirebaseUser) null));
            cachedUser.addSource(source, new Observer<AuthResource<FirebaseUser>>() {
                @Override
                public void onChanged(AuthResource<FirebaseUser> userAuthResource) {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);
                }
            });
        }
    }

    public void logOut() {
        Log.d(TAG, "logOut: logging out...");
        cachedUser.setValue(AuthResource.logout());
    }

    public LiveData<AuthResource<FirebaseUser>> getAuthUser() {
        return cachedUser;
    }

}

















