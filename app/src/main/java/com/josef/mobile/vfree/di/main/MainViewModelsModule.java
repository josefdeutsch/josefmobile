package com.josef.mobile.vfree.di.main;

import androidx.lifecycle.ViewModel;

import com.josef.mobile.vfree.di.ViewModelKey;
import com.josef.mobile.vfree.ui.main.MainViewModel;
import com.josef.mobile.vfree.ui.main.about.AboutViewModel;
import com.josef.mobile.vfree.ui.main.archive.ArchiveViewModel;
import com.josef.mobile.vfree.ui.main.home.HomeViewModel;
import com.josef.mobile.vfree.ui.main.impr.ImpressionsViewModel;
import com.josef.mobile.vfree.ui.main.post.PostsViewModel;
import com.josef.mobile.vfree.ui.main.profiles.ProfilesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    public abstract ViewModel bindProfileViewModel(HomeViewModel viewModel);

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

    @Binds
    @IntoMap
    @ViewModelKey(AboutViewModel.class)
    public abstract ViewModel bindAboutViewModel(AboutViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfilesViewModel.class)
    public abstract ViewModel bindProfilesViewModel(ProfilesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImpressionsViewModel.class)
    public abstract ViewModel bindImpressionsViewModel(ImpressionsViewModel viewModel);

}
