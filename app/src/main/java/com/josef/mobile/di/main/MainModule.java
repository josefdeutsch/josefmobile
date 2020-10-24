package com.josef.mobile.di.main;


import com.josef.mobile.data.remote.Endpoints;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static Endpoints provideMainApi(Retrofit retrofit) {
        return retrofit.create(Endpoints.class);
    }


}

