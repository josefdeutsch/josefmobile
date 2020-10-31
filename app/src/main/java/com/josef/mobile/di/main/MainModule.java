package com.josef.mobile.di.main;


import com.bumptech.glide.RequestManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.remote.Endpoints;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.archive.helpers.firebase.FirebaseUpload;
import com.josef.mobile.ui.main.archive.helpers.firebase.FirebaseUploadHelper;
import com.josef.mobile.ui.main.archive.helpers.local.ArchiveDatabase;
import com.josef.mobile.ui.main.archive.helpers.local.ArchiveDatabaseHelper;
import com.josef.mobile.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.ui.main.post.helpers.remote.EndpointsObserver;
import com.josef.mobile.ui.main.post.helpers.remote.EndpointsObserverHelper;
import com.josef.mobile.utils.CommonUtils;

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
    static PostRecyclerAdapter providePostRecyclerAdapter(RequestManager requestManager, DataManager dataManager, CommonUtils commonUtils) {
        return new PostRecyclerAdapter(requestManager, dataManager, commonUtils);
    }

    @MainScope
    @Provides
    static MainActivity provideMainActivity(MainActivity mainActivity) {
        return mainActivity;
    }


    @MainScope
    @Provides
    static FirebaseUpload provideFirebaseUpload(FirebaseUploadHelper firebaseUploadHelper) {
        return firebaseUploadHelper;
    }

    @MainScope
    @Provides
    static ArchiveDatabase provideArchiveDatabase(ArchiveDatabaseHelper archiveDatabaseHelper) {
        return archiveDatabaseHelper;
    }

    @MainScope
    @Provides
    static EndpointsObserver provideEndpointsObserver(EndpointsObserverHelper endpointsObserverHelper) {
        return endpointsObserverHelper;
    }


    /** @Provides
     @MainScope static PostRecyclerAdapter.PostRecyclerViewOnClickListener getClickListener(PostsFragment postsFragment) {
     return postsFragment;
     }**/

}

