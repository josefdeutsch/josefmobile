package com.josef.mobile.free.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.util.AppPreferences;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.josef.mobile.util.Config.JOSEPHOPENINGSTATEMENT;
import static com.josef.mobile.util.Config.WORKREQUEST_KEYTAST_OUTPUT;
import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class ContentDetailFragment extends Fragment {

    private FavouriteViewModel mFavouriteViewMode;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;
    public View layoutInflater;
    private com.josef.mobile.free.util.VideoPlayer videoPlayer;
    private String mDownloadId;
    private int index;
    private SimpleExoPlayerView mExoPlayerView;
    private int mResumeWindow;
    private long mResumePosition;
    public ImageButton mPlayButton;
    private Object lock;
    public static final String JSON_METADATA = "metadata";
    public static final String JSON_NAME = "name";
    public static final String JSON_PNG = "png";
    public static final String JSON_URL = "url";
    public static final String STATE_RESUME_WINDOW = "com.josef.mobile.free.ui.ContentDetailFragment.resumeWindow";
    public static final String STATE_RESUME_POSITION = "com.josef.mobile.free.ui.ContentDetailFragment.resumePosition";

    private AtomicBoolean atomicBoolean = new AtomicBoolean();
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
        }
         mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);

        setupUi();

        videoPlayer = new com.josef.mobile.free.util.VideoPlayer(getActivity(), layoutInflater, mExoPlayerView, mResumePosition, mResumeWindow);
        mArticle.setText("Sculpture: " + index);

        setupExoPlayer(mDownloadId, index);

        setupSubHeader(mDownloadId, index);

        setupPlayButton(mDownloadId, index);
        setupToggleDatabase(mDownloadId, index);

        return layoutInflater;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        videoPlayer.matchesExoPlayerFullScreenConfig();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (videoPlayer != null) videoPlayer.withdrawExoPlayer();
        mResumePosition = videoPlayer.getmResumePosition();
        mResumeWindow = videoPlayer.getmResumeWindow();

    }

    public void onDestroyView() {
        super.onDestroyView();

    }

    public void onPlayerBackState() {
        if (lock != null) {
            videoPlayer.onPlayerBackState();
        }
    }

    private void setupUi() {
        mFavouriteViewMode = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        mExoPlayerView = layoutInflater.findViewById(R.id.exoplayer);
        mArticle = layoutInflater.findViewById(R.id.article_title);
        mArticleByLine = layoutInflater.findViewById(R.id.article_byline);
        mButtonDataBase = layoutInflater.findViewById(R.id.button_favorite2);
        mPlayButton = layoutInflater.findViewById(R.id.exo_play);
    }


    private void setupPlayButton(final String downloadId, final int index) {
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lock == null) {
                    lock = new Object();
                    setupMediaSource(downloadId, index);
                } else {
                    videoPlayer.onPlay();
                }
            }
        });
    }

    public void setupMediaSource(final String downloadId, final int index) {
        if (downloadId != null) {
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    try {
                                        videoPlayer.setupMediaSource(output, index);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    });
        }
    }

    public void setupExoPlayer(final String downloadId, final int index) {
        if (downloadId != null) {
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    try {
                                        videoPlayer.setupThumbNailSource(output, index);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    videoPlayer.initExoPlayer();
                                    videoPlayer.initFullscreenButton();
                                    videoPlayer.initFullscreenDialog();

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
                                                Boolean lock = mPrefs.getBoolean("locked", false);
                                                if(!lock) {
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
                                                mPrefs.edit().putBoolean("locked", true).apply();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        compoundButton.setChecked(false);
                                                    }
                                                }, 3000);
                                            } else {

                                            }
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