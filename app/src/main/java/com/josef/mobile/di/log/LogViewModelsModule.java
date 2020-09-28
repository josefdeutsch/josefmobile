package com.josef.mobile.di.log;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.di.ViewModelKey;
import com.josef.mobile.ui.googlesignin.GoogleSignInViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LogViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(GoogleSignInViewModel.class)
    public abstract ViewModel bindGoogleViewModel(GoogleSignInViewModel viewModel);
}

