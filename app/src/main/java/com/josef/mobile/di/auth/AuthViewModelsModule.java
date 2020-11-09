package com.josef.mobile.di.auth;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.di.ViewModelKey;
import com.josef.mobile.ui.auth.AuthInputViewModel;
import com.josef.mobile.ui.auth.AuthViewModel;
import com.josef.mobile.ui.auth.sign.SignViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindGoogleViewModel(AuthViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SignViewModel.class)
    public abstract ViewModel bindSignViewModel(SignViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthInputViewModel.class)
    public abstract ViewModel bindInputViewModel(AuthInputViewModel viewModel);
}

