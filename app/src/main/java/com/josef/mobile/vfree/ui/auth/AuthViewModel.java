package com.josef.mobile.vfree.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.josef.mobile.vfree.SessionManager;
import com.josef.mobile.vfree.ui.auth.email.EmailLogin;
import com.josef.mobile.vfree.ui.auth.google.GoogleLogin;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public final class AuthViewModel extends BaseViewModel {

    @NonNull
    private final SessionManager sessionManager;
    @NonNull
    private final GoogleLogin googleLogin;
    @NonNull
    private final EmailLogin emailLogin;


    @Inject
    AuthViewModel(@NonNull SessionManager sessionManager,
                  @NonNull GoogleLogin googleLogin,
                  @NonNull EmailLogin emailLogin) {
        this.sessionManager = sessionManager;
        this.googleLogin = googleLogin;
        this.emailLogin = emailLogin;
    }

    public LiveData<AuthResource<User>> observeAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }

    public void authenticateWithGoogle(@NonNull Task<GoogleSignInAccount> googleSignInAccount) {
        LiveData<AuthResource<User>> source
                = LiveDataReactiveStreams.fromPublisher(googleLogin.authenticateWithGoogle(googleSignInAccount));
        sessionManager.observeAuthResource(source);
    }

    public void authenticateWithEmail( @NonNull String email,
                                       @NonNull String password) {
        LiveData<AuthResource<User>> source
                = LiveDataReactiveStreams.fromPublisher(emailLogin.authenticateWithEmailAccount(email, password));
        sessionManager.observeAuthResource(source);
    }


}