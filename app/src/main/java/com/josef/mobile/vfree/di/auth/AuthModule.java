package com.josef.mobile.vfree.di.auth;

import com.josef.mobile.vfree.ui.auth.email.AppEmailLogin;
import com.josef.mobile.vfree.ui.auth.email.EmailLogin;
import com.josef.mobile.vfree.ui.auth.google.AppGoogleLogin;
import com.josef.mobile.vfree.ui.auth.google.GoogleLogin;

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
