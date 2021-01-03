package com.josef.josefmobile.di.main;

import androidx.lifecycle.ViewModel;

import com.josef.josefmobile.di.ViewModelKey;
import com.josef.josefmobile.ui.main.MainViewModel;
import com.josef.josefmobile.ui.main.archive.ArchiveViewModel;
import com.josef.josefmobile.ui.main.post.PostsViewModel;
import com.josef.josefmobile.ui.main.profile.ProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    public abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel.class)
    public abstract ViewModel bindPostViewModel(PostsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArchiveViewModel.class)
    public abstract ViewModel bindArchiveViewModel(ArchiveViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);
}
