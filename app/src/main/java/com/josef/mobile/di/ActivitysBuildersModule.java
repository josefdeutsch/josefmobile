package com.josef.mobile.di;

import com.josef.mobile.di.auth.IntroModule;
import com.josef.mobile.di.auth.IntroScope;
import com.josef.mobile.di.auth.IntroViewModelsModule;
import com.josef.mobile.di.log.AuthModule;
import com.josef.mobile.di.log.AuthScope;
import com.josef.mobile.di.log.AuthViewModelsModule;
import com.josef.mobile.di.main.MainFragmentBuildersModule;
import com.josef.mobile.di.main.MainModule;
import com.josef.mobile.di.main.MainScope;
import com.josef.mobile.di.main.MainViewModelsModule;
import com.josef.mobile.di.player.PlayerModule;
import com.josef.mobile.di.player.PlayerScope;
import com.josef.mobile.di.player.PlayerViewModelsModule;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.intro.IntroActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.player.PlayerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitysBuildersModule {

    @IntroScope
    @ContributesAndroidInjector(
            modules = {
                    IntroViewModelsModule.class,
                    IntroModule.class
            }
    )
    abstract IntroActivity contributeIntroctivity();

    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthModule.class,
                    AuthViewModelsModule.class
            }
    )
    abstract AuthActivity contributeAuthActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

    @PlayerScope
    @ContributesAndroidInjector(
            modules = {
                    PlayerModule.class,
                    PlayerViewModelsModule.class
            }
    )
    abstract PlayerActivity contributePlayerActivity();

}
