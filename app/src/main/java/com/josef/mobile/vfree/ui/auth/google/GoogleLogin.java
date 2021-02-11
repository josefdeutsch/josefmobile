package com.josef.mobile.vfree.ui.auth.google;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Flowable;

public interface GoogleLogin {

    @NotNull
    Flowable<AuthResource<User>> authenticateWithGoogle(@NotNull Task<GoogleSignInAccount> googleSignInAccountSingle);
}
