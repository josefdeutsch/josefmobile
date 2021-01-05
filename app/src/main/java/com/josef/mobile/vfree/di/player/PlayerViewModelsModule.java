package com.josef.mobile.vfree.di.player;


import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.di.ViewModelKey;
import com.josef.mobile.vfree.ui.player.PlayerViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class PlayerViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    public abstract ViewModel bindPlayerViewModel(PlayerViewModel viewModel);
}

