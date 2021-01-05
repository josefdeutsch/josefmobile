package com.josef.mobile.vfree.di;

import androidx.lifecycle.ViewModelProvider;

import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);

}