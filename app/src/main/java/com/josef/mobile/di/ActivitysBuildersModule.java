package com.josef.mobile.di;

import com.josef.mobile.di.auth.AuthModule;
import com.josef.mobile.di.auth.AuthScope;
import com.josef.mobile.di.auth.AuthViewModelsModule;
import com.josef.mobile.di.main.MainFragmentBuildersModule;
import com.josef.mobile.di.main.MainModule;
import com.josef.mobile.di.main.MainScope;
import com.josef.mobile.di.main.MainViewModelsModule;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.main.MainActivity;

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
