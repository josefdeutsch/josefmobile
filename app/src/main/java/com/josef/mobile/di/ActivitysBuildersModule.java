package com.josef.mobile.di;

import com.josef.mobile.di.auth.AuthModule;
import com.josef.mobile.di.auth.AuthScope;
import com.josef.mobile.di.auth.AuthViewModelsModule;
import com.josef.mobile.di.log.LogModule;
import com.josef.mobile.di.log.LogScope;
import com.josef.mobile.di.log.LogViewModelsModule;
import com.josef.mobile.di.main.MainFragmentBuildersModule;
import com.josef.mobile.di.main.MainModule;
import com.josef.mobile.di.main.MainScope;
import com.josef.mobile.di.main.MainViewModelsModule;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.googlesignin.GoogleSignInActivity;
import com.josef.mobile.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitysBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthViewModelsModule.class,
                    AuthModule.class
            }
    )
    abstract AuthActivity contributeAuthActivity();

    @LogScope
    @ContributesAndroidInjector(
            modules = {
                    LogModule.class,
                    LogViewModelsModule.class,
            }
    )
    abstract GoogleSignInActivity contributeGoogleActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

}
