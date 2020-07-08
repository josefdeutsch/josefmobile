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

public class AdFragment extends Fragment {

    private FavouriteViewModel mFavouriteViewMode;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
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

    public AdFragment() {
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
        layoutInflater = inflater.inflate(R.layout.fragment_ad, container, false);

        setupUi();

        videoPlayer = new com.josef.mobile.free.util.VideoPlayer(getActivity(), layoutInflater, mExoPlayerView, mResumePosition, mResumeWindow);

        setupExoPlayer(mDownloadId, index);

        setupPlayButton(mDownloadId, index);

        //mExoPlayerView.setControllerAutoShow(false);

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
        mExoPlayerView = layoutInflater.findViewById(R.id.exoplayers);
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