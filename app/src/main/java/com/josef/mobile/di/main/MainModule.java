package com.josef.mobile.di.main;


import com.josef.mobile.net.main.MainApi;
import com.josef.mobile.ui.main.post.PostRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }

    @MainScope
    @Provides
    static PostRecyclerAdapter provideAdapter() {
        return new PostRecyclerAdapter();
    }
}

