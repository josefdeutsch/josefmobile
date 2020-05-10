package com.josef.mobile.free.ui;

import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.Action;
import com.josef.mobile.AppPreferences;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static com.josef.mobile.Config.JOSEPHOPENINGSTATEMENT;
import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.STATE_RESUME_POSITION;
import static com.josef.mobile.Config.STATE_RESUME_POSITION_MIN_FRAME;
import static com.josef.mobile.Config.STATE_RESUME_WINDOW;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.WORKERDOWNLOADID;

public class ContentDetailFragment extends Fragment {

    private FavouriteViewModel mFavouriteViewMode;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;
    public View layoutInflater;
    private VideoPlayer videoPlayer;
    private String mDownloadId;
    private int index;
    private SimpleExoPlayerView mExoPlayerView;
    private int mResumeWindow;
    private long mResumePosition;
    private long mResumePosition_min;
    public ImageButton mPlayButton;
    private ImaAdsLoader imaAdsLoader;
    private Object lock;


    public ContentDetailFragment() {
    }

    public static ContentDetailFragment newInstance(String which, int index) {
        ContentDetailFragment fragment = new ContentDetailFragment();
        Bundle args = new Bundle();
        args.putString(WORKERDOWNLOADID, which);
        args.putInt(VIEWPAGERDETAILKEY, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDownloadId = getArguments().getString(WORKERDOWNLOADID);
            index = getArguments().getInt(VIEWPAGERDETAILKEY);
        }
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mResumePosition_min = savedInstanceState.getLong(STATE_RESUME_POSITION_MIN_FRAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);

        setupUi();

        videoPlayer = new VideoPlayer(getActivity(), layoutInflater, mExoPlayerView, mResumePosition, mResumeWindow);
        mArticle.setText("Skulpture" + index);

        setupExoPlayer(mDownloadId, index);
        setupSubHeader(mDownloadId, index);
        setupPlayButton(mDownloadId, index);
        setupToggleDatabase(mDownloadId, index);
        setupToggleFavorite(mDownloadId, index);
        //   videoPlayer.matchesExoPlayerFullScreenConfig();
        return layoutInflater;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putLong(STATE_RESUME_POSITION_MIN_FRAME, mResumePosition_min);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // videoPlayer.matchesExoPlayerFullScreenConfig();
    }

    @Override
    public void onStop() {
        super.onStop();
        //if(videoPlayer!=null) videoPlayer.withdrawExoPlayer();
        //mResumePosition = videoPlayer.getmResumePosition();
        //mResumeWindow = videoPlayer.getmResumeWindow();

    }

    public void onDestroyView() {
        super.onDestroyView();
        // if(videoPlayer!=null) videoPlayer.withdrawExoPlayer();
        // mResumePosition = videoPlayer.getmResumePosition();
        //   mResumeWindow = videoPlayer.getmResumeWindow();

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
        mButtonFavorite = layoutInflater.findViewById(R.id.button_favorite);
        mButtonDataBase = layoutInflater.findViewById(R.id.button_favorite2);
        mPlayButton = layoutInflater.findViewById(R.id.exo_play);
    }

    public void addActionToUI(Action action) {
        action.performAction(mDownloadId, index);
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
        if(downloadId!=null) {
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

    private void setupToggleFavorite(final String downloadId, final int index) {
        if (downloadId != null) {
            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    mButtonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                                            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
                                            scaleAnimation.setDuration(500);
                                            BounceInterpolator bounceInterpolator = new BounceInterpolator();
                                            scaleAnimation.setInterpolator(bounceInterpolator);
                                            compoundButton.startAnimation(scaleAnimation);
                                            if (isChecked) {
                                                final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_content), "ready to share..!", Snackbar.LENGTH_LONG)
                                                        .setAction("OK", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                addItemsToAppPreference(output, index);
                                                                compoundButton.setChecked(false);
                                                            }
                                                        }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

                                                snackbar.setAnchorView(getActivity().findViewById(R.id.fab));
                                                snackbar.show();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        compoundButton.setChecked(false);
                                                    }
                                                }, 3000);
                                            }
                                        }
                                    });
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
                                                final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.bottom_app_bar_coord), "save item..?!", Snackbar.LENGTH_LONG)
                                                        .setAction("OK", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                addItemtsToDataBase(output, index);
                                                                compoundButton.setChecked(false);
                                                            }
                                                        }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

                                                snackbar.setAnchorView(getActivity().findViewById(R.id.fab));
                                                snackbar.show();

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

    public void addItemsToAppPreference(final String output, final int index) {

        try {
            JSONArray input = new JSONArray(output);
            JSONObject container = input.getJSONObject(index);
            JSONObject metadata = (JSONObject) container.get("metadata");
            String url = (String) metadata.get("url");
            AppPreferences.clearNameList(getContext());
            ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
            meta.add(JOSEPHOPENINGSTATEMENT + System.lineSeparator());
            meta.add(url + System.lineSeparator());
            AppPreferences.clearNameList(getContext());
            AppPreferences.setName(getContext(), meta);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addItemtsToDataBase(final String output, final int index) {

        try {
            JSONArray input = new JSONArray(output);
            JSONObject container = input.getJSONObject(index);
            JSONObject metadata = (JSONObject) container.get("metadata");
            String name = (String) metadata.get("name");
            String png = (String) metadata.get("png");
            String url = (String) metadata.get("url");
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
                                        JSONObject metadata = (JSONObject) container.get("metadata");
                                        String name = (String) metadata.get("name");
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
        String output = data.getString(KEY_TASK_OUTPUT);
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
}