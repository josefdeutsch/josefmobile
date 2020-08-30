package com.josef.mobile.free.ui.detail;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.josef.mobile.free.ui.detail.ViewModelDetail.JSON_METADATA;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.JSON_PNG;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.JSON_URL;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_BOOLEAN_VALUE;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_RESUME_POSITION;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_RESUME_WINDOW;
import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class ContentDetailFragment extends VideoPlayerFragment {

    private FavouriteViewModel mFavouriteViewMode;
    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;

    public ContentDetailFragment() {
    }

    public static ContentDetailFragment newInstance(String which, int index) {
        ContentDetailFragment fragment = new ContentDetailFragment();
        Bundle args = new Bundle();
        args.putString(WORKREQUEST_DOWNLOADID, which);
        args.putInt(VIEWPAGERDETAILKEY, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDownloadId = getArguments().getString(WORKREQUEST_DOWNLOADID);
            index = getArguments().getInt(VIEWPAGERDETAILKEY);
        }
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_BOOLEAN_VALUE);
        }
        mViewModelDetail = ViewModelProviders.of(this).get(ViewModelDetail.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);
/** if (mExoPlayerFullscreen) {
 openFullscreenDialog();
 matchesExoPlayerFullScreenConfig();
 }**/
        setupUi();

        mArticle.setText("Sculpture: " + index);

        setupView(mArticleByLine, new Supplier() {
            @Override
            public void supply() {
                doWork(new Worker() {
                    @Override
                    public void execute(String input, int index) {
                        try {
                            String name = mViewModelDetail.getJsonName(input,index);
                            mArticleByLine.setText(name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        setupView(mArtWork, new Supplier() {
            @Override
            public void supply() {
                doWork(new Worker() {
                    @Override
                    public void execute(String input, int index) {
                        setupThumbNailSource2(input,index);
                    }
                });
            }
        });

        setupView(mArtWork, new View.OnClickListener() {
            int button01pos = 0;

            @Override
            public void onClick(View v) {
                if (button01pos == 0) {
                    mPlayerView.showController();
                    button01pos = 1;
                } else if (button01pos == 1) {
                    mPlayerView.hideController();
                    button01pos = 0;
                }
            }
        });

        setupView(mPlayButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lock == null) {
                    lock = new Object();
                    doWork(new Worker() {
                        @Override
                        public void execute(String input, int index) {
                            initExoPlayer(getContext());
                            initFullscreenDialog();
                            setupMediaSource2(input, index);
                        }
                    });
                } else {
                    if (!mPlayer.getPlayWhenReady())
                        mPlayer.setPlayWhenReady(true);
                }
            }
        });

        setupView(mFullScreenButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen) {
                    openFullscreenDialog();
                } else
                    closeFullscreenDialog();
            }
        });

        setupView(mButtonDataBase, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                doWork(new Worker() {
                    @Override
                    public void execute(final String input, final int index) {
                        ScaleAnimation scaleAnimation = mViewModelDetail.getScaleAnimation();
                        buttonView.startAnimation(scaleAnimation);
                        if (isChecked) {
                            final Snackbar snackbar = Snackbar.make(
                                    getActivity().findViewById(R.id.main_content), "save item..?!", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String png = null;
                                            String url = null;
                                            try {
                                                png = mViewModelDetail.getJsonPng(input,index);
                                                url = mViewModelDetail.getJsonUrl(input,index);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Favourite favourite = new Favourite(png, url, 0);
                                            mFavouriteViewMode.insert(favourite);
                                            buttonView.setChecked(false);
                                        }
                                    }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                            snackbar.setAnchorView(getActivity().findViewById(R.id.fab));
                            snackbar.show();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                buttonView.setChecked(false);
                            }
                        }, 3000);
                    }
                });
            }
        });

        return layoutInflater;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_BOOLEAN_VALUE, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        withdrawExoPlayer();
        if (mPlayer != null) mPlayer.release();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onPlayerBackState() {
        if (lock != null) {
            if (mPlayer.getPlayWhenReady())
                mPlayer.setPlayWhenReady(false);
        }
    }

    private void setupUi() {
        mFavouriteViewMode = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        mPlayerView = layoutInflater.findViewById(R.id.player);
        mFullScreenIcon = mPlayerView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = mPlayerView.findViewById(R.id.exo_fullscreen_button);
        mPlayButton = layoutInflater.findViewById(R.id.exo_play);
        mArticle = layoutInflater.findViewById(R.id.article_title);
        mArticleByLine = layoutInflater.findViewById(R.id.article_byline);
        mButtonDataBase = layoutInflater.findViewById(R.id.button_favorite2);
        mArtWork = layoutInflater.findViewById(R.id.exo_artwork);
        mProgressBar = layoutInflater.findViewById(R.id.progress);
    }



    private Player.EventListener playerListener = new Player.EventListener() {


        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {


            } else if (playbackState == Player.STATE_READY) {


            } else if (playbackState == Player.STATE_ENDED) {
                mPlayerView.getPlayer().release();
            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }


        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    };

    @Override
    protected Player.EventListener buildPlayerEventListener() {
        return playerListener;
    }

}
