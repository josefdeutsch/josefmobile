package com.josef.mobile.vfree.di.main;

import com.josef.mobile.vfree.ui.main.about.AboutFragment;
import com.josef.mobile.vfree.ui.main.archive.ArchiveFragment;
import com.josef.mobile.vfree.ui.main.events.EventsFragment;
import com.josef.mobile.vfree.ui.main.home.HomeFragment;
import com.josef.mobile.vfree.ui.main.post.PostsFragment;
import com.josef.mobile.vfree.ui.main.profiles.ProfilesFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract HomeFragment provideProfileFragment();

    @ContributesAndroidInjector
    abstract PostsFragment constributePostFragment();

    @ContributesAndroidInjector
    abstract ArchiveFragment constributeArchiveFragment();

    @ContributesAndroidInjector
    abstract AboutFragment constributeInfoFragment();

    @ContributesAndroidInjector
    abstract EventsFragment constributeEventFragment();

    @ContributesAndroidInjector
    abstract ProfilesFragment constributeProfilesFragment();

}
