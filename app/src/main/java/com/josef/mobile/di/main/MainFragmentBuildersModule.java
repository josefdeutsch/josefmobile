package com.josef.mobile.di.main;


import com.josef.mobile.ui.main.archive.ArchiveFragment;
import com.josef.mobile.ui.main.post.content.first.FirstPostFragment;
import com.josef.mobile.ui.main.post.content.second.SecondPostFragment;
import com.josef.mobile.ui.main.post.content.third.ThirdPostFragment;
import com.josef.mobile.ui.main.profile.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ProfileFragment provideProfileFragment();

    @ContributesAndroidInjector
    abstract FirstPostFragment constributeFirstPostFragment();

    @ContributesAndroidInjector
    abstract SecondPostFragment constributeSecondPostsFragment();

    @ContributesAndroidInjector
    abstract ThirdPostFragment constributeThirdPostsFragment();

    @ContributesAndroidInjector
    abstract ArchiveFragment constributeArchiveFragment();

}
