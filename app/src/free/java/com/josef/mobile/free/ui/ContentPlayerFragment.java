package com.josef.mobile.free.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
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
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.STATE_PLAYER_FULLSCREEN;
import static com.josef.mobile.Config.STATE_RESUME_POSITION;
import static com.josef.mobile.Config.STATE_RESUME_POSITION_MIN_FRAME;
import static com.josef.mobile.Config.STATE_RESUME_WINDOW;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.WORKER_DOWNLOADID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContentPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentPlayerFragment extends Fragment {


    // https://github.com/googleads/googleads-ima-android

    private String mId;
    private int mIndex;
    private SimpleExoPlayerView mExoPlayerView;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private int mResumeWindow;
    private long mResumePosition;
    private long mResumePosition_min;
    public ImageButton playButton;
    private ImageButton pauseButton;
    private static final String TAG = "PlayerFragment";
    private View mView;
    private ImaAdsLoader imaAdsLoader;
    private Object lock;


    Target target = new Target() {
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

    public ContentPlayerFragment() {
        // Required empty public constructor
    }

    public static ContentPlayerFragment newInstance(String downloadid, int index) {
        ContentPlayerFragment fragment = new ContentPlayerFragment();
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, downloadid);
        args.putInt(VIEWPAGERDETAILKEY, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(WORKER_DOWNLOADID);
            mIndex = getArguments().getInt(VIEWPAGERDETAILKEY);
        }
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mResumePosition_min = savedInstanceState.getLong(STATE_RESUME_POSITION_MIN_FRAME);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        //  imaAdsLoader = new ImaAdsLoader(getContext(), getAdTagUri());

    }

    private ToggleButton playpauseButton;
    private Boolean mExoPlayerIsSetup = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_player, container, false);

        playButton = mView.findViewById(R.id.exo_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lock == null) {
                    setupMediaSource();

                    mExoPlayerView.getPlayer().seekTo(mResumePosition);
                    mExoPlayerView.getPlayer().setPlayWhenReady(true);

                } else {
                    if (!mExoPlayerView.getPlayer().getPlayWhenReady())
                        mExoPlayerView.getPlayer().setPlayWhenReady(true);
                }
            }
        });
        mExoPlayerView = (SimpleExoPlayerView) mView.findViewById(R.id.exoplayer);

        initFullscreenDialog();
        initFullscreenButton();
        initExoPlayer();
        setupThumbNailSource();

        Log.d(TAG, "onCreateView: " + " count of playerfragments");

        /**  boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
         if (haveResumePosition) {
         mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
         }**/
        return mView;
    }
    public void setId(String downloadid, int index){
        mId = downloadid;
        mIndex = index;
    }

    public void onPlayExecute() {
        if (lock != null) {
            if (!mExoPlayerView.getPlayer().getPlayWhenReady())
                mExoPlayerView.getPlayer().setPlayWhenReady(true);
        }
    }

    public void onPlayerBackState() {
        if (lock != null) {
            if (mExoPlayerView.getPlayer().getPlayWhenReady())
                mExoPlayerView.getPlayer().setPlayWhenReady(false);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putLong(STATE_RESUME_POSITION_MIN_FRAME, mResumePosition_min);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "withdrawExoPlayer: " + mResumePosition);
        matchesExoPlayerFullScreenConfig();
    }

    @Override
    public void onStop() {
        super.onStop();
        // mResumePosition_min = mExoPlayerView.getPlayer().getCurrentPosition();

        withdrawExoPlayer();
        //   mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
        // Toast.makeText(getActivity(), String.valueOf(mResumePosition),
        //       Toast.LENGTH_SHORT).show();
//        mExoPlayerView.getPlayer().release();

    }

    @Override
    public void onDetach() {
        super.onDetach();

        //imaAdsLoader.release();
    }

    public void setupMediaSource() {

        lock = new Object();

        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mId))
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                String output = getViewPagerContent(workInfo);
                                try {
                                    JSONArray input = new JSONArray(output);
                                    JSONObject container = input.getJSONObject(mIndex);
                                    JSONObject metadata = (JSONObject) container.get("metadata");
                                    String url = (String) metadata.get("url");
                                    supplyExoPlayer(url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                            }
                        }
                    }
                });
    }

    private void setupThumbNailSource() {
        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mId))
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                String output = getViewPagerContent(workInfo);
                                try {
                                    JSONArray input = new JSONArray(output);
                                    JSONObject container = input.getJSONObject(mIndex);
                                    JSONObject metadata = (JSONObject) container.get("metadata");
                                    String png = (String) metadata.get("png");
                                    postThumbnailIntoExoplayer(png);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                            }
                        }
                    }
                });
    }

    @Nullable
    private String getViewPagerContent(@NotNull WorkInfo workInfo) {
        Data data = workInfo.getOutputData();
        String output = data.getString(KEY_TASK_OUTPUT);
        return output;
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
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
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) mView.findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    private void initFullscreenButton() {

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

    private void initExoPlayer() {
        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mId))
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                                LoadControl loadControl = new DefaultLoadControl();
                                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
                                mExoPlayerView.setPlayer(player);
                                //mExoPlayerView.getPlayer().addListener(this);

                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                            }
                        }
                    }
                });

    }

    private void supplyExoPlayer(String videoURL) {

        MediaSource videoSource = buildMediaSource(videoURL);
        mExoPlayerView.getPlayer().prepare(videoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);

    }

    private MediaSource buildMediaSource(String videoURL) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "ExoPlayer"));
        final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        return new ExtractorMediaSource(Uri.parse(videoURL), dataSourceFactory, extractorsFactory, null, null);
    }

    private void matchesExoPlayerFullScreenConfig() {
        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }

    private final void withdrawExoPlayer() {
        mExoPlayerView.getPlayer().setPlayWhenReady(false);
        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
            ;
            //   Log.d(TAG, "withdrawExoPlayer: "+mResumePosition);
            mExoPlayerView.getPlayer().release();
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog.dismiss();
        }
    }

    private void postThumbnailIntoExoplayer(String png) {
        Picasso.get().load(png).into(target);
    }

    private Uri getAdTagUri() {
        return Uri.parse("https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dskippablelinear&correlator=");
    }



    /**
     * private void supplyExoPlayer(String videoURL) {
     * <p>
     * DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "ExoPlayer"));
     * final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
     * MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL), dataSourceFactory, extractorsFactory, null, null);
     * <p>
     * MediaSource mediaSourceWithAds = new ImaAdsMediaSource(
     * mediaSource, dataSourceFactory,
     * imaAdsLoader,
     * mExoPlayerView.getOverlayFrameLayout());
     * mExoPlayerView.getPlayer().prepare(mediaSourceWithAds);
     * // mExoPlayerView.getPlayer().setPlayWhenReady(true);
     * // mExoPlayerView.getPlayer().seekTo(mResumePosition);
     * <p>
     * }
     **/
}