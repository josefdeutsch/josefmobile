package com.josef.josefmobile.di;

import com.josef.josefmobile.di.auth.AuthModule;
import com.josef.josefmobile.di.auth.AuthScope;
import com.josef.josefmobile.di.auth.AuthViewModelsModule;
import com.josef.josefmobile.di.err.ErrorModule;
import com.josef.josefmobile.di.err.ErrorScope;
import com.josef.josefmobile.di.err.ErrorViewModelsModule;
import com.josef.josefmobile.di.main.MainFragmentBuildersModule;
import com.josef.josefmobile.di.main.MainModule;
import com.josef.josefmobile.di.main.MainScope;
import com.josef.josefmobile.di.main.MainViewModelsModule;
import com.josef.josefmobile.di.player.PlayerModule;
import com.josef.josefmobile.di.player.PlayerScope;
import com.josef.josefmobile.di.player.PlayerViewModelsModule;
import com.josef.josefmobile.di.splash.SplashModule;
import com.josef.josefmobile.di.splash.SplashScope;
import com.josef.josefmobile.di.splash.SplashViewModelsModule;
import com.josef.josefmobile.ui.auth.AuthActivity;
import com.josef.josefmobile.ui.auth.option.account.SignActivity;
import com.josef.josefmobile.ui.auth.option.verification.VerificationActivity;
import com.josef.josefmobile.ui.err.ErrorActivity;
import com.josef.josefmobile.ui.main.MainActivity;
import com.josef.josefmobile.ui.player.PlayerActivity;
import com.josef.josefmobile.ui.splash.SplashActivity;

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
