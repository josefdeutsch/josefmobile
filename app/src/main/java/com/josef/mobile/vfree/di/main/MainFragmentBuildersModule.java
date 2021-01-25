package com.josef.mobile.vfree.di.main;

import com.josef.mobile.vfree.ui.main.archive.ArchiveFragment;
import com.josef.mobile.vfree.ui.main.about.AboutFragment;
import com.josef.mobile.vfree.ui.main.post.PostsFragment;
import com.josef.mobile.vfree.ui.main.profile.ProfileFragment;
import com.josef.mobile.vfree.ui.main.tv.TvFragment;

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
    abstract TvFragment constributeTvFragment();

    @ContributesAndroidInjector
    abstract AboutFragment constributeInfoFragment();

}
