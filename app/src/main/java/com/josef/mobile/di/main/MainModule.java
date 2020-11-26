package com.josef.mobile.di.main;


import android.content.Context;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.archive.helpers.firebase.FirebaseUpload;
import com.josef.mobile.ui.main.archive.helpers.firebase.FirebaseUploadHelper;
import com.josef.mobile.ui.main.archive.helpers.local.ArchiveDatabase;
import com.josef.mobile.ui.main.archive.helpers.local.ArchiveDatabaseHelper;
import com.josef.mobile.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.ui.main.post.helpers.remote.AppEndpointsObserver;
import com.josef.mobile.ui.main.post.helpers.remote.EndpointsObserver;
import com.josef.mobile.utils.UtilManager;

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
    static EndpointsObserver provideEndpointsObserver(AppEndpointsObserver appEndpointsObserver) {
        return appEndpointsObserver;
    }


    /** @Provides
     @MainScope static PostRecyclerAdapter.PostRecyclerViewOnClickListener getClickListener(PostsFragment postsFragment) {
     return postsFragment;
     }**/

}

