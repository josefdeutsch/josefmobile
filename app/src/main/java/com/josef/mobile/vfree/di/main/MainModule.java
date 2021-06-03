package com.josef.mobile.vfree.di.main;


import android.content.Context;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.about.adapters.AboutRecyclerViewAdapter;
import com.josef.mobile.vfree.ui.main.about.remote.AppDownloadAboutEndpoints;
import com.josef.mobile.vfree.ui.main.about.remote.DownloadAboutEndpoints;
import com.josef.mobile.vfree.ui.main.archive.ArchiveRecyclerViewAdapter;
import com.josef.mobile.vfree.ui.main.archive.fire.AppFirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.fire.FirebaseUpload;
import com.josef.mobile.vfree.ui.main.archive.local.AppArchiveDatabase;
import com.josef.mobile.vfree.ui.main.archive.local.ArchiveDatabase;
import com.josef.mobile.vfree.ui.main.home.res.AppDownloadHomeEndpoints;
import com.josef.mobile.vfree.ui.main.home.res.DownloadHomeEndpoints;
import com.josef.mobile.vfree.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.vfree.ui.main.post.remote.AppDownloadEndpoints;
import com.josef.mobile.vfree.ui.main.post.remote.DownloadEndpoints;
import com.josef.mobile.vfree.ui.main.home.ViewPagerAdapter;
import com.josef.mobile.vfree.ui.main.profiles.adapters.ProfileRecyclerViewAdapter;
import com.josef.mobile.vfree.ui.main.profiles.remote.AppDownloadProfileEndpoints;
import com.josef.mobile.vfree.ui.main.profiles.remote.DownloadProfileEndpoints;
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
    static AboutRecyclerViewAdapter provideAboutRecyclerViewAdapter(Context context) {
        return new AboutRecyclerViewAdapter(context);
    }

    @MainScope
    @Provides
    static ProfileRecyclerViewAdapter provideProfileRecyclerViewAdapter(Context context, RequestManager requestManager) {
        return new ProfileRecyclerViewAdapter(context, requestManager);
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
    static DownloadHomeEndpoints provideResourceObserver(AppDownloadHomeEndpoints appResourceObserver) {
        return appResourceObserver;
    }

    @MainScope
    @Provides
    static Credentials provideCredentials(AppDataBaseCredentials appDataBaseCredentials) {
        return appDataBaseCredentials;
    }

    @MainScope
    @Provides
    static DownloadAboutEndpoints provideAboutDownloadEndpoints(AppDownloadAboutEndpoints downloadAboutEndpoints) {
        return downloadAboutEndpoints;
    }

    @MainScope
    @Provides
    static DownloadProfileEndpoints provideProfileDownloadEndpoints(AppDownloadProfileEndpoints downloadAboutEndpoints) {
        return downloadAboutEndpoints;
    }



}

