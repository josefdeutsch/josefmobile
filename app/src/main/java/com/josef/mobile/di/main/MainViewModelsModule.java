package com.josef.mobile.di.main;

import androidx.lifecycle.ViewModel;

import com.josef.mobile.di.ViewModelKey;
import com.josef.mobile.ui.main.archive.ArchiveViewModel;
import com.josef.mobile.ui.main.post.PostsViewModel;
import com.josef.mobile.ui.main.profile.ProfileViewModel;

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
    public abstract ViewModel bindPostsViewModel(PostsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArchiveViewModel.class)
    public abstract ViewModel bindArchiveViewModel(ArchiveViewModel viewModel);

}
