package com.josef.josefmobile.di.main;

import com.josef.josefmobile.ui.main.archive.ArchiveFragment;
import com.josef.josefmobile.ui.main.info.InfoFragment;
import com.josef.josefmobile.ui.main.post.PostsFragment;
import com.josef.josefmobile.ui.main.profile.ProfileFragment;

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

    @ContributesAndroidInjector
    abstract InfoFragment constributeInfoFragment();

}
