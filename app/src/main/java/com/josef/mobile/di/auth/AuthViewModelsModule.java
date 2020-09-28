package com.josef.mobile.di.auth;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.di.ViewModelKey;
import com.josef.mobile.ui.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);
}

