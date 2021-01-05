package com.josef.mobile.vfree.di;

import com.josef.mobile.vfree.di.auth.AuthModule;
import com.josef.mobile.vfree.di.auth.AuthScope;
import com.josef.mobile.vfree.di.auth.AuthViewModelsModule;
import com.josef.mobile.vfree.di.err.ErrorModule;
import com.josef.mobile.vfree.di.err.ErrorScope;
import com.josef.mobile.vfree.di.err.ErrorViewModelsModule;
import com.josef.mobile.vfree.di.main.MainFragmentBuildersModule;
import com.josef.mobile.vfree.di.main.MainModule;
import com.josef.mobile.vfree.di.main.MainScope;
import com.josef.mobile.vfree.di.main.MainViewModelsModule;
import com.josef.mobile.vfree.di.player.PlayerModule;
import com.josef.mobile.vfree.di.player.PlayerScope;
import com.josef.mobile.vfree.di.player.PlayerViewModelsModule;
import com.josef.mobile.vfree.di.splash.SplashModule;
import com.josef.mobile.vfree.di.splash.SplashScope;
import com.josef.mobile.vfree.di.splash.SplashViewModelsModule;
import com.josef.mobile.vfree.ui.auth.AuthActivity;
import com.josef.mobile.vfree.ui.auth.option.account.SignActivity;
import com.josef.mobile.vfree.ui.auth.option.verification.VerificationActivity;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.player.PlayerActivity;
import com.josef.mobile.vfree.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitysBuildersModule {


    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthModule.class,
                    AuthViewModelsModule.class
            }
    )
    abstract AuthActivity contributeAuthActivity();

    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthModule.class,
                    AuthViewModelsModule.class
            }
    )
    abstract SignActivity contributeSignActivity();

    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthModule.class,
                    AuthViewModelsModule.class
            }
    )
    abstract VerificationActivity contributeVerificationActivity();

    @PlayerScope
    @ContributesAndroidInjector(
            modules = {
                    PlayerModule.class,
                    PlayerViewModelsModule.class
            }
    )
    abstract PlayerActivity contributePlayerActivity();

    @SplashScope
    @ContributesAndroidInjector(
            modules = {
                    SplashModule.class,
                    SplashViewModelsModule.class
            }
    )
    abstract SplashActivity contributeSplashActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

    @ErrorScope
    @ContributesAndroidInjector(
            modules = {
                    ErrorViewModelsModule.class,
                    ErrorModule.class,
            }
    )
    abstract ErrorActivity contributeErrorActivity();


}
