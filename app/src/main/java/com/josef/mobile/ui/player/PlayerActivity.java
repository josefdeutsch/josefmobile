package com.josef.mobile.ui.player;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.josef.mobile.R;
import com.josef.mobile.ui.err.ErrorActivity;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.err.ErrorActivity.ACTIVITY_KEY;
import static com.josef.mobile.ui.err.ErrorActivity.EXECEPTION_KEY;
import static com.josef.mobile.utils.AppConstants.REQUEST_INDEX;
import static com.josef.mobile.utils.AppConstants.STATE_BOOLEAN_VALUE;
import static com.josef.mobile.utils.AppConstants.STATE_RESUME_POSITION;
import static com.josef.mobile.utils.AppConstants.STATE_RESUME_WINDOW;

public class PlayerActivity extends DaggerAppCompatActivity {


    @Inject
    Dialog mFullScreenDialog;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    SimpleExoPlayer mPlayer;

    @Inject
    ProgressiveMediaSource.Factory videoSource;

    protected boolean mExoPlayerFullscreen = false;
    protected int mResumeWindow;
    protected long mResumePosition;

    private PlayerViewModel viewModel;

    private int index;
    @BindView(R.id.exo_fullscreen_button)
    FrameLayout mFullScreenButton;
    @BindView(R.id.exo_fullscreen_icon)
    ImageView mFullScreenIcon;
    @BindView(R.id.exoplayer)
    PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        if (savedInstanceState == null) index = getIntent().getIntExtra(REQUEST_INDEX, 0);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_BOOLEAN_VALUE);
        }

        viewModel = new ViewModelProvider(this, providerFactory).get(PlayerViewModel.class);
        subscribeObserver();
        openFullscreenDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_BOOLEAN_VALUE, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }

    private void subscribeObserver() {
        viewModel.authenticateWithEndpoint(index);
        viewModel.observeEndpoints().observe(this, listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        break;
                    }
                    case SUCCESS: {
                        if (listResource.data.getUrl() != null)
                            setupMediaSource(listResource.data.getUrl());
                        break;
                    }
                    case ERROR: {
                        Intent intent = new Intent(this, ErrorActivity.class);
                        intent.putExtra(ACTIVITY_KEY, this.getComponentName().getClassName());
                        intent.putExtra(EXECEPTION_KEY, listResource.message);
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PlayerActivity.this,
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        startActivity(intent, bundle);

                        finishAfterTransition();
                        break;
                    }
                }
            }
        });
        //  viewModel.observeEndpoints().removeObservers(this);
    }

    protected void openFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenButton.setOnClickListener(v -> finish());
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_skrink));
        mFullScreenDialog.show();
    }


    protected void setupMediaSource(String url) {
        mPlayerView.setPlayer(mPlayer);
        mPlayer.prepare(videoSource.createMediaSource(Uri.parse(url)));
        mPlayer.seekTo(mResumePosition);
        mPlayer.setPlayWhenReady(true);
    }

    protected void releaseExoPlayer() {
        if (mPlayerView != null && mPlayer != null) {
            mResumeWindow = mPlayer.getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayer.getContentPosition());
            mPlayer.release();
        }
        mFullScreenDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseExoPlayer();
    }
}