package com.josef.mobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.josef.mobile.models.Player;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String TAG = "DaggerExample";
    // data
    private MediatorLiveData<Resource<Player>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager() {

    }

    public void select(final LiveData<Resource<Player>> source) {
        if (cachedUser != null) {
            cachedUser.setValue(Resource.loading(null));
            cachedUser.addSource(source, new Observer<Resource<Player>>() {
                @Override
                public void onChanged(Resource<Player> userAuthResource) {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);
                }
            });
        }
    }


    public LiveData<Resource<Player>> getAuthUser() {
        return cachedUser;
    }

}

















