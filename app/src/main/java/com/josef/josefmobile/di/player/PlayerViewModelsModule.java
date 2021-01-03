package com.josef.josefmobile.di.player;


import androidx.lifecycle.ViewModel;

import com.josef.josefmobile.di.ViewModelKey;
import com.josef.josefmobile.ui.player.PlayerViewModel;

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

