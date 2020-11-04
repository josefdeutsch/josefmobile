package com.josef.mobile.ui.auth.google;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;

import io.reactivex.Flowable;

public interface GoogleLogin {

    Flowable<AuthResource<User>> authenticateWithGoogle(Task<GoogleSignInAccount> googleSignInAccountSingle);
}
