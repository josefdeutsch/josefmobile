package com.josef.josefmobile.di.auth;

import com.josef.josefmobile.ui.auth.email.AppEmailLogin;
import com.josef.josefmobile.ui.auth.email.EmailLogin;
import com.josef.josefmobile.ui.auth.google.AppGoogleLogin;
import com.josef.josefmobile.ui.auth.google.GoogleLogin;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AuthModule {

    @AuthScope
    @Provides
    static EmailLogin provideEmailLogin(AppEmailLogin emailLogin) {
        return emailLogin;
    }

    @AuthScope
    @Provides
    static GoogleLogin provideGoogleLogin(AppGoogleLogin googleLogin) {
        return googleLogin;
    }


}
