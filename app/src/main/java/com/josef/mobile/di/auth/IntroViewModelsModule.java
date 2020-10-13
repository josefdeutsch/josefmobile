package com.josef.mobile.di.auth;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.di.ViewModelKey;
import com.josef.mobile.ui.main.post.IntroViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class IntroViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel.class)
    public abstract ViewModel bindAuthViewModel(IntroViewModel viewModel);
}

