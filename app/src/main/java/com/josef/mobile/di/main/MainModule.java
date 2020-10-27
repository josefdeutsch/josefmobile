package com.josef.mobile.di.main;


import com.bumptech.glide.RequestManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.remote.Endpoints;
import com.josef.mobile.ui.main.post.PostRecyclerAdapter;

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


    @MainScope
    @Provides
    static PostRecyclerAdapter providePostRecyclerAdapter(RequestManager requestManager, DataManager dataManager) {
        return new PostRecyclerAdapter(requestManager, dataManager);
    }

}

