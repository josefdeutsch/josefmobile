package com.josef.mobile.ui.googlesignin;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josef.mobile.FirebaseManager;
import com.josef.mobile.ui.intro.AuthResource;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GoogleSignInViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    // inject
    private final FirebaseManager sessionManager; // @Singleton scoped dependency
    private final FirebaseAuth authApi; // @AuthScope scoped dependency

    @Inject
    public GoogleSignInViewModel(FirebaseAuth authApi, FirebaseManager sessionManager) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
        Log.d(TAG, "AuthViewModel: viewmodel is working...");
    }

    public LiveData<AuthResource<FirebaseUser>> observeAuthState() {
        return sessionManager.getAuthUser();
    }


    public void authenticateWithId(int userId) {
        Log.d(TAG, "attemptLogin: attempting to login.");
        sessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<FirebaseUser>> queryUserId(int userId) {
        Flowable<FirebaseUser> flowable = Observable.just(authApi.getCurrentUser())
                .subscribeOn(Schedulers.io())
                .toFlowable(BackpressureStrategy.BUFFER);

        return LiveDataReactiveStreams.fromPublisher(flowable
                // instead of calling onError, do this
                .onErrorReturn(new Function<Throwable, FirebaseUser>() {
                    @Override
                    public FirebaseUser apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: " + throwable.toString());
                        FirebaseUser errorUser = null;
                        return errorUser;
                    }
                })
                // wrap User object in AuthResource
                .map(new Function<FirebaseUser, AuthResource<FirebaseUser>>() {
                    @Override
                    public AuthResource<FirebaseUser> apply(FirebaseUser user) throws Exception {
                        if (user == null) return AuthResource.error("Could not authenticate", null);
                        return AuthResource.authenticated(user);
                    }
                })
                .subscribeOn(Schedulers.io()));
    }


}
