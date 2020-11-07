package com.josef.mobile.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.josef.mobile.SessionManager;
import com.josef.mobile.ui.auth.email.EmailLogin;
import com.josef.mobile.ui.auth.google.GoogleLogin;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.base.BaseViewModel;

import javax.inject.Inject;

public class AuthViewModel extends BaseViewModel {

    private final SessionManager sessionManager;
    private final GoogleLogin googleLogin;
    private final EmailLogin emailLogin;

    private static final String TAG = "AuthViewModel";

    @Inject
    AuthViewModel(SessionManager sessionManager, GoogleLogin googleLogin, EmailLogin emailLogin) {
        this.sessionManager = sessionManager;
        this.googleLogin = googleLogin;
        this.emailLogin = emailLogin;
    }

    public LiveData<AuthResource<User>> observeAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }

    public void authenticateWithGoogle(Task<GoogleSignInAccount> googleSignInAccount) {
        LiveData<AuthResource<User>> source
                = LiveDataReactiveStreams.fromPublisher(googleLogin.authenticateWithGoogle(googleSignInAccount));
        sessionManager.observeAuthResource(source);
    }

    public void authenticateWithEmail(String email, String password) {
        LiveData<AuthResource<User>> source
                = LiveDataReactiveStreams.fromPublisher(emailLogin.authenticateWithEmailAccount(email, password));
        sessionManager.observeAuthResource(source);
    }
}