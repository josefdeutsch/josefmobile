package com.josef.josefmobile.di.splash;


import androidx.lifecycle.ViewModel;

import com.josef.josefmobile.di.ViewModelKey;
import com.josef.josefmobile.ui.splash.SplashViewModel;

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

