package com.josef.mobile.di.main;

import androidx.lifecycle.ViewModel;

import com.josef.mobile.di.ViewModelKey;
import com.josef.mobile.ui.main.archive.ArchiveViewModel;
import com.josef.mobile.ui.main.post.content.first.FirstFragmentViewModel;
import com.josef.mobile.ui.main.post.content.second.SecondPostFragmentViewModel;
import com.josef.mobile.ui.main.post.content.third.ThirdPostFragmentViewModel;
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
    @ViewModelKey(FirstFragmentViewModel.class)
    public abstract ViewModel bindFirstPostsViewModel(FirstFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SecondPostFragmentViewModel.class)
    public abstract ViewModel bindSecondPostsViewModel(SecondPostFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ThirdPostFragmentViewModel.class)
    public abstract ViewModel bindThirdPostsViewModel(ThirdPostFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArchiveViewModel.class)
    public abstract ViewModel bindArchiveViewModel(ArchiveViewModel viewModel);

}
