package com.josef.mobile.vfree.ui.auth.email;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;

import io.reactivex.Flowable;

public interface EmailLogin {

    @NonNull
    Flowable<AuthResource<User>> authenticateWithEmailAccount(@NonNull String email,
                                                              @NonNull String password);
}
