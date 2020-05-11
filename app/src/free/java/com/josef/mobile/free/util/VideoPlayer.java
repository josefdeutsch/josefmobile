package com.josef.mobile.free.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.ext.ima.ImaAdsMediaSource;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.josef.josefmobile.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoPlayer {

    private Context mContext;
    private View layoutInflater;
    private SimpleExoPlayerView mExoPlayerView;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private long mResumePosition;
    private int mResumeWindow;
    private boolean mExoPlayerFullscreen = false;
    private ImaAdsLoader imaAdsLoader;

    private String targetUrl = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dskippablelinear&correlator=";

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mExoPlayerView.setDefaultArtwork(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    public long getmResumePosition() {
        return mResumePosition;
    }

    public int getmResumeWindow() {
        return mResumeWindow;
    }

    public void onPlayerBackState() {
        if (mExoPlayerView.getPlayer().getPlayWhenReady())
            mExoPlayerView.getPlayer().setPlayWhenReady(false);
    }

    public void onPlay() {

        if (!mExoPlayerView.getPlayer().getPlayWhenReady())
            mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }

    public VideoPlayer(Context mContext, View layoutInflater, SimpleExoPlayerView mExoPlayerView, long mResumePosition, int mResumeWindow) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mExoPlayerView = mExoPlayerView;
        this.mResumePosition = mResumePosition;
        this.mResumeWindow = mResumeWindow;
        imaAdsLoader = new ImaAdsLoader(mContext, getAdTagUri());
    }

    @Nullable
    public void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) layoutInflater.findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_expand));
    }

    public void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });

    }

    public void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mContext), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

    }

    public void setupThumbNailSource(final String output, final int index) throws JSONException {
        JSONArray input = new JSONArray(output);
        JSONObject container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get("metadata");
        String png = (String) metadata.get("png");
        postThumbnailIntoExoplayer(png);
    }

    public void setupMediaSource(final String output, final int index) throws JSONException {
        JSONArray input = new JSONArray(output);
        JSONObject container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get("metadata");
        String url = (String) metadata.get("url");
        supplyExoPlayer(url);
        mExoPlayerView.getPlayer().seekTo(mResumePosition);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }

    private void supplyExoPlayer(String videoURL) {

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "ExoPlayer"));
        final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL), dataSourceFactory, extractorsFactory, null, null);

        MediaSource mediaSourceWithAds = new ImaAdsMediaSource(
                mediaSource, dataSourceFactory,
                imaAdsLoader,
                mExoPlayerView.getOverlayFrameLayout());
        mExoPlayerView.getPlayer().prepare(mediaSourceWithAds);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }

    public void matchesExoPlayerFullScreenConfig() {
        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }

    public final void withdrawExoPlayer() {

        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mExoPlayerView.getPlayer().setPlayWhenReady(false);
            if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
                mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
                mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
                mExoPlayerView.getPlayer().release();
            }
            if (mFullScreenDialog != null) {
                mFullScreenDialog.dismiss();
            }
        }
    }

    private void postThumbnailIntoExoplayer(String png) {
        Picasso.get().load(png).into(target);
    }

    private Uri getAdTagUri() {
        return Uri.parse(targetUrl);
    }

}
