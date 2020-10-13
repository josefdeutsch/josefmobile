package com.josef.mobile.di.auth;


import com.josef.mobile.net.auth.AuthApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public abstract class IntroModule {

    @IntroScope
    @Provides
    static AuthApi provideSessionApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }
}
