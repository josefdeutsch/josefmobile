package com.josef.mobile.vfree.di.main;


import android.content.Context;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.archive.ArchiveRecyclerViewAdapter;
import com.josef.mobile.vfree.ui.main.archive.fire.AppFirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.fire.FirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.local.AppArchiveDatabase;
import com.josef.mobile.vfree.ui.main.archive.local.ArchiveDatabase;
import com.josef.mobile.vfree.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.vfree.ui.main.post.remote.AppEndpointsObserver;
import com.josef.mobile.vfree.ui.main.post.remote.EndpointsObserver;
import com.josef.mobile.vfree.ui.main.profile.ViewPagerAdapter;
import com.josef.mobile.vfree.ui.main.profile.res.AppResourceObserver;
import com.josef.mobile.vfree.ui.main.profile.res.ResourceObserver;
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
    static EndpointsObserver provideEndpointsObserver(AppEndpointsObserver appEndpointsObserver) {
        return appEndpointsObserver;
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

}

