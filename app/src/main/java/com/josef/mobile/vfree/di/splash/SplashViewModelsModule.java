package com.josef.mobile.vfree.di.splash;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.di.ViewModelKey;
import com.josef.mobile.vfree.ui.splash.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SplashViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    public abstract ViewModel bindSplashViewModel(SplashViewModel viewModel);
}

