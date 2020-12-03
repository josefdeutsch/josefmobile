package com.josef.mobile.di;

import com.josef.mobile.di.auth.AuthModule;
import com.josef.mobile.di.auth.AuthScope;
import com.josef.mobile.di.auth.AuthViewModelsModule;
import com.josef.mobile.di.err.ErrorModule;
import com.josef.mobile.di.err.ErrorScope;
import com.josef.mobile.di.err.ErrorViewModelsModule;
import com.josef.mobile.di.main.MainFragmentBuildersModule;
import com.josef.mobile.di.main.MainModule;
import com.josef.mobile.di.main.MainScope;
import com.josef.mobile.di.main.MainViewModelsModule;
import com.josef.mobile.di.player.PlayerModule;
import com.josef.mobile.di.player.PlayerScope;
import com.josef.mobile.di.player.PlayerViewModelsModule;
import com.josef.mobile.di.splash.SplashModule;
import com.josef.mobile.di.splash.SplashScope;
import com.josef.mobile.di.splash.SplashViewModelsModule;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.auth.option.account.SignActivity;
import com.josef.mobile.ui.auth.option.verification.VerificationActivity;
import com.josef.mobile.ui.err.ErrorActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.player.PlayerActivity;
import com.josef.mobile.ui.splash.SplashActivity;

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
