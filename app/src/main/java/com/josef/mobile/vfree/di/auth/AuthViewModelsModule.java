package com.josef.mobile.vfree.di.auth;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.di.ViewModelKey;
import com.josef.mobile.vfree.ui.auth.AuthInputViewModel;
import com.josef.mobile.vfree.ui.auth.AuthViewModel;
import com.josef.mobile.vfree.ui.auth.option.account.SignViewModel;
import com.josef.mobile.vfree.ui.auth.option.verification.VerificationViewModel;

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
    @ViewModelKey(VerificationViewModel.class)
    public abstract ViewModel bindVerificationViewModel(VerificationViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthInputViewModel.class)
    public abstract ViewModel bindInputViewModel(AuthInputViewModel viewModel);
}

