package com.josef.mobile.di.main;

import com.josef.mobile.ui.main.archive.ArchiveFragment;
import com.josef.mobile.ui.main.post.PostsFragment;
import com.josef.mobile.ui.main.profile.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ProfileFragment provideProfileFragment();

    @ContributesAndroidInjector
    abstract PostsFragment constributePostFragment();

    @ContributesAndroidInjector
    abstract ArchiveFragment constributeArchiveFragment();

}
