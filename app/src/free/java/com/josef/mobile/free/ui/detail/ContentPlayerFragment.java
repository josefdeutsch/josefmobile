package com.josef.mobile.free.ui.detail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.josef.josefmobile.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

public class ContentPlayerFragment extends ContentBaseFragment {

    protected Dialog mFullScreenDialog;
    protected FrameLayout mFullScreenButton;
    protected ImageView mFullScreenIcon;
    protected ImageView mArtWork;
    protected SimpleExoPlayer mPlayer;
    protected PlayerView mPlayerView;
    protected ImageButton mPlayButton;

    protected boolean mExoPlayerFullscreen = false;
    protected int mResumeWindow;
    protected long mResumePosition;
    protected View layoutInflater;


    protected void initExoPlayer(Context context) {
        DefaultBandwidthMeter.Builder bandwidthMeter = new DefaultBandwidthMeter.Builder(context);
        BandwidthMeter bandwidthMeters = bandwidthMeter.build();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultRenderersFactory(context), trackSelector, loadControl, null, bandwidthMeters);
    }

    protected void setupMediaSource(final String output, final int index, final int query) throws JSONException {
        mPlayerView.setPlayer(mPlayer);
        //Player.EventListener playerListener = buildPlayerEventListener();
        //mPlayerView.getPlayer().addListener(playerListener);
        String url = mViewModelDetail.getJsonUrl(output, index);
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), getActivity().getString(R.string.app_name)));
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url));
        mPlayer.prepare(videoSource);
        mPlayer.seekTo(mResumePosition);
        mPlayer.setPlayWhenReady(true);


    }

    protected void matchesExoPlayerFullScreenConfig() {
        if (!mExoPlayerFullscreen) return;

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_skrink));
        mFullScreenDialog.show();

    }

    protected void releaseExoPlayer() {
        if (mPlayerView != null && mPlayer != null){
            mResumeWindow = mPlayer.getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayer.getContentPosition());
            mPlayer.release();
        }


        releaseFullScreenDialog();
    }

    protected void releaseFullScreenDialog() {
        if (mFullScreenDialog == null) return;
        mFullScreenDialog.dismiss();
    }


    protected void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen) closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    protected void openFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    protected void closeFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout) layoutInflater.findViewById(R.id.main_media_frame)).addView(mPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_expand));
    }

    public void onPlayerBackState() {
        if (lock != null && mPlayer.getPlayWhenReady()) mPlayer.setPlayWhenReady(false);
    }

    public void setupThumbNailSource(final String output, final int index, final int query) throws JSONException {
        String png = mViewModelDetail.getJsonPng(output, index,query);
        Picasso.get().load(png).into(target);
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mArtWork.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    };

}

