package com.josef.mobile.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.josef.mobile.SessionManager;
import com.josef.mobile.ui.auth.google.GoogleLogin;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.base.BaseViewModel;

import javax.inject.Inject;

public class AuthViewModel extends BaseViewModel {

    private final SessionManager sessionManager;
    private final GoogleLogin googleLogin;
    private static final String TAG = "AuthViewModel";

    @Inject
    AuthViewModel(SessionManager sessionManager, GoogleLogin googleLogin) {
        this.sessionManager = sessionManager;
        this.googleLogin = googleLogin;
    }

    public LiveData<AuthResource<User>> observeAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }

    public void authenticateWithGoogle(Task<GoogleSignInAccount> googleSignInAccount) {
        LiveData<AuthResource<User>> source
                = LiveDataReactiveStreams.fromPublisher(googleLogin.authenticateWithGoogle(googleSignInAccount));
        sessionManager.observeAuthResource(source);
    }
}