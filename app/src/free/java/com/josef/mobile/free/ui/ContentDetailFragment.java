package com.josef.mobile.free.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

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
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.josef.mobile.ui.ErrorActivity.TAG;
import static com.josef.mobile.util.Config.WORKREQUEST_KEYTAST_OUTPUT;
import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class ContentDetailFragment extends VideoPlayer {

    private FavouriteViewModel mFavouriteViewMode;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;
    private String mDownloadId;
    private int index;
    public ImageButton mPlayButton;
    private Object lock;

    public static final String JSON_METADATA = "metadata";
    public static final String JSON_NAME = "name";
    public static final String JSON_PNG = "png";
    public static final String JSON_URL = "url";
    public static final String STATE_RESUME_WINDOW = "com.josef.mobile.free.ui.ContentDetailFragment.resumeWindow";
    public static final String STATE_RESUME_POSITION = "com.josef.mobile.free.ui.ContentDetailFragment.resumePosition";
    public static final String STATE_BOOLEAN_VALUE = "com.josef.mobile.free.ui.ContentDetailFragment.value";

    private SharedPreferences mPrefs;


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

        initFullscreenDialog();


        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);


        setupUi();
        setupExoPlayer(mDownloadId, index);
        if (mExoPlayerFullscreen) {
            openFullscreenDialog();
            matchesExoPlayerFullScreenConfig();
        }

        mArticle.setText("Sculpture: " + index);
        //setupSubHeader(mDownloadId, index);

        setupThumbnailSource(mDownloadId, index);

        setupPlayButton(mDownloadId, index);

        setupToggleDatabase(mDownloadId, index);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPlayer != null) mPlayer.release();
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


    private void setupPlayButton(final String downloadId, final int index) {
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lock == null) {
                    lock = new Object();
                    setupMediaSource(downloadId, index);
                } else {
                    if (!mPlayer.getPlayWhenReady())
                        mPlayer.setPlayWhenReady(true);
                }
            }
        });
    }

    public void setupMediaSource(final String downloadId, final int index) {
        if (downloadId != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    setupMediaSource2(output, index);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });


        }
    }

    public void setupExoPlayer(final String downloadId, final int index) {
        if (downloadId != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    initExoPlayer(getContext());
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
        }
    }

    public void setupThumbnailSource(final String downloadId, final int index) {

        mArtWork.setOnClickListener(new View.OnClickListener() {
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

        if (downloadId != null) {
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    try {
                                        setupThumbNailSource2(output, index);
                                        Log.d(TAG, "onChanged: " + " thumbnailchanged");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public void setupToggleDatabase(final String downloadId, final int index) {
        if (downloadId != null) {
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
                                    scaleAnimation.setDuration(500);
                                    BounceInterpolator bounceInterpolator = new BounceInterpolator();
                                    scaleAnimation.setInterpolator(bounceInterpolator);

                                    mButtonDataBase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                                            compoundButton.startAnimation(scaleAnimation);
                                            if (isChecked) {
                                                //     Boolean lock = mPrefs.getBoolean("locked", false);
                                                //   if(!lock) {
                                                final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_content), "save item..?!", Snackbar.LENGTH_LONG)
                                                        .setAction("OK", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                addItemtsToDataBase(output, index);
                                                                compoundButton.setChecked(false);
                                                            }
                                                        }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

                                                snackbar.setAnchorView(getActivity().findViewById(R.id.fab));
                                                snackbar.show();
                                            }
                                            // mPrefs.edit().putBoolean("locked", true).apply();

                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    compoundButton.setChecked(false);
                                                }
                                            }, 3000);
                                            // } else {

                                            //}
                                        }
                                    });
                                }
                            }
                        }
                    });
        }
    }

    public void addItemtsToDataBase(final String output, final int index) {

        try {
            JSONArray input = new JSONArray(output);
            JSONObject container = input.getJSONObject(index);
            JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
            String url = (String) metadata.get(JSON_URL);
            String png = (String) metadata.get(JSON_PNG);
            Favourite favourite = new Favourite(png, url, 0);
            mFavouriteViewMode.insert(favourite);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setupSubHeader(final String downloadId, final int index) {
        if (downloadId != null) {
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    String output = getViewPagerContent(workInfo);
                                    try {
                                        JSONArray input = new JSONArray(output);
                                        JSONObject container = input.getJSONObject(index);
                                        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
                                        String name = (String) metadata.get(JSON_NAME);
                                        name = removeLastChar(name);
                                        mArticleByLine.setText(name);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
        }
    }


    @Nullable
    private String getViewPagerContent(@NotNull WorkInfo workInfo) {
        Data data = workInfo.getOutputData();
        String output = data.getString(WORKREQUEST_KEYTAST_OUTPUT);
        return output;
    }

    @Nullable
    private IdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = EspressoIdlingResource.getIdlingResource();
        }
        return mIdlingResource;
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 22);
    }
}