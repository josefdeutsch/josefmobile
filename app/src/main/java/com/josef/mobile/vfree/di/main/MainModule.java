package com.josef.mobile.vfree.di.main;


import android.content.Context;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.di.splash.SplashScope;
import com.josef.mobile.vfree.ui.main.archive.ArchiveRecyclerViewAdapter;
import com.josef.mobile.vfree.ui.main.archive.fire.AppFirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.fire.FirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.local.AppArchiveDatabase;
import com.josef.mobile.vfree.ui.main.archive.local.ArchiveDatabase;
import com.josef.mobile.vfree.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.vfree.ui.main.post.remote.AppDownloadEndpoints;
import com.josef.mobile.vfree.ui.main.post.remote.DownloadEndpoints;
import com.josef.mobile.vfree.ui.main.profile.ViewPagerAdapter;
import com.josef.mobile.vfree.ui.main.profile.res.AppResourceObserver;
import com.josef.mobile.vfree.ui.main.profile.res.ResourceObserver;
import com.josef.mobile.vfree.ui.main.store.AppDataBaseCredentials;
import com.josef.mobile.vfree.ui.main.store.Credentials;
import com.josef.mobile.vfree.utils.UtilManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {


    @MainScope
    @Provides
    static PostRecyclerAdapter providePostRecyclerAdapter(RequestManager requestManager, DataManager dataManager, UtilManager utilManager, Context context) {
        return new PostRecyclerAdapter(requestManager, dataManager, utilManager, context);
    }

    @MainScope
    @Provides
    static ArchiveRecyclerViewAdapter provideArchiveRecyclerViewAdapter(RequestManager requestManager) {
        return new ArchiveRecyclerViewAdapter(requestManager);
    }

    @MainScope
    @Provides
    static ViewPagerAdapter provideViewPagerAdapter(RequestManager requestManager, Context context) {
        return new ViewPagerAdapter(requestManager, context);
    }


    @MainScope
    @Provides
    static FirebaseUpload provideFirebaseUpload(AppFirebaseUpload appFirebaseUpload) {
        return appFirebaseUpload;
    }

    @MainScope
    @Provides
    static ArchiveDatabase provideArchiveDatabase(AppArchiveDatabase appArchiveDatabase) {
        return appArchiveDatabase;
    }

    @MainScope
    @Provides
    static DownloadEndpoints provideDownloadEndpoints(AppDownloadEndpoints appDownloadEndpoints) {
        return appDownloadEndpoints;
    }

    @MainScope
    @Provides
    static ResourceObserver provideResourceObserver(AppResourceObserver appResourceObserver) {
        return appResourceObserver;
    }

    /** @Provides
     @MainScope static PostRecyclerAdapter.PostRecyclerViewOnClickListener getClickListener(PostsFragment postsFragment) {
     return postsFragment;
     }**/
    @MainScope
    @Provides
    static Credentials provideCredentials(AppDataBaseCredentials appDataBaseCredentials) {
        return appDataBaseCredentials;
    }
}

