package com.josef.mobile.free.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.josef.josefmobile.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.josef.mobile.ui.ErrorActivity.TAG;

public class VideoPlayer extends Fragment {

    protected Dialog mFullScreenDialog;
    protected FrameLayout mFullScreenButton;
    protected ImageView mFullScreenIcon;
    protected ImageView mArtWork;
    protected SimpleExoPlayer mPlayer;
    protected PlayerView mPlayerView;
    protected ProgressBar mProgressBar;
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
        mPlayer = ExoPlayerFactory.newSimpleInstance(context,new DefaultRenderersFactory(context), trackSelector, loadControl,null,bandwidthMeters);


    }

    protected void setupMediaSource2(final String output,final int index) {

        try {
            mPlayerView.setPlayer(mPlayer);
            JSONArray input = new JSONArray(output);
            JSONObject container = null;
            container = input.getJSONObject(index);
            JSONObject metadata = (JSONObject) container.get("metadata");
            String url = (String) metadata.get("url");
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), getActivity().getString(R.string.app_name)));
            MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            mPlayer.prepare(videoSource);
            mPlayer.seekTo(mResumePosition);
            mPlayer.setPlayWhenReady(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void matchesExoPlayerFullScreenConfig() {
        if (mExoPlayerFullscreen) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }

    protected void withdrawExoPlayer() {

        if (mPlayerView != null && mPlayer != null) {
            mResumeWindow = mPlayer.getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayer.getContentPosition());
            mPlayer.release();
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog.dismiss();
        }
    }

    protected void initFullScreenButton() {
        PlayerControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen){
                    openFullscreenDialog();
                }
                else
                    closeFullscreenDialog();
            }
        });
    }

    @Nullable
    protected void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
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

    private String mPng;

    private void postThumbnailIntoExoplayer(String png) {
        mPng = png;
        if (png != null && !png.isEmpty()) {
            Log.d(TAG, "postThumbnailIntoExoplayer: "+"after");
            Picasso.get().load(png).into(target);
        } else {

        }
    }
    public void setupThumbNailSource2(final String output, final int index) throws JSONException {
        JSONArray input = new JSONArray(output);
        JSONObject container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get("metadata");
        String png = (String) metadata.get("png");
        Log.d(TAG, "setupThumbNailSource: "+"before");
        postThumbnailIntoExoplayer(png);
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(TAG, "onBitmapLoaded: ");
            mArtWork.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Log.d(TAG, "onBitmapFailed: ");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

}

