package com.josef.josefmobile.ui.auth.email;

import com.josef.josefmobile.ui.auth.AuthResource;
import com.josef.josefmobile.ui.auth.model.User;

import io.reactivex.Flowable;

public interface EmailLogin {

    Flowable<AuthResource<User>> authenticateWithEmailAccount(String email, String password);
}
