package com.josef.mobile.vfree.di.player;

import android.app.Dialog;
import android.content.Context;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.josef.mobile.vfree.ui.player.PlayerActivity;
import com.josef.mobile.vfree.ui.player.remote.EndpointObserver;
import com.josef.mobile.vfree.ui.player.remote.EndpointObserverHelper;
import com.josef.mobile.R;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class PlayerModule {

    @PlayerScope
    @Provides
    static EndpointObserver provideEndpointsObserver(EndpointObserverHelper endpointObserverHelper) {
        return endpointObserverHelper;
    }

    @PlayerScope
    @Provides
    static SimpleExoPlayer provideSimpleExoplayer(Context context) {
        DefaultBandwidthMeter.Builder bandwidthMeter = new DefaultBandwidthMeter.Builder(context);
        BandwidthMeter bandwidthMeters = bandwidthMeter.build();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(context, new DefaultRenderersFactory(context), trackSelector, loadControl, null, bandwidthMeters);
    }

    @PlayerScope
    @Provides
    static ProgressiveMediaSource.Factory provideProgressiveMediaSource(Context context) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getString(R.string.app_name)));
        return new ProgressiveMediaSource.Factory(dataSourceFactory);
    }

    @PlayerScope
    @Provides
    static Dialog provideFullScreenDialog(PlayerActivity playerActivity) {
        return new Dialog(playerActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                playerActivity.finish();
                super.onBackPressed();
            }
        };
    }
}
