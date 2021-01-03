package com.josef.josefmobile.di.splash;

import com.josef.josefmobile.ui.splash.remote.AppDownloadEndpoints;
import com.josef.josefmobile.ui.splash.remote.DownloadEndpoints;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SplashModule {

    @SplashScope
    @Provides
    static DownloadEndpoints provideDownloadEndpoints(AppDownloadEndpoints appDownloadEndpoints) {
        return appDownloadEndpoints;
    }
}
