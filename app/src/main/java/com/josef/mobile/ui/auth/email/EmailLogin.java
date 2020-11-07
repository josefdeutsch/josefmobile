package com.josef.mobile.ui.auth.email;

import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;

import io.reactivex.Flowable;

public interface EmailLogin {

    Flowable<AuthResource<User>> authenticateWithEmailAccount(String email, String password);
}
