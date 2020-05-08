package com.josef.mobile.free.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.AppPreferences;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.STATE_PLAYER_FULLSCREEN;
import static com.josef.mobile.Config.STATE_RESUME_POSITION;
import static com.josef.mobile.Config.STATE_RESUME_POSITION_MIN_FRAME;
import static com.josef.mobile.Config.STATE_RESUME_WINDOW;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.WORKERDOWNLOADID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ContentDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private FavouriteViewModel favouriteViewModel;
    public ImageView mImageButton;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
    public TextView article;
    public TextView article_by_line;
    public View layoutInflater;
    public View layoutinflater;
    // TODO: Rename and change types of parameters
    private String mDownloadId;
    private int index;

    private String mId;
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
    private ImaAdsLoader imaAdsLoader;
    private Object lock;

    // Player Event Listener.....


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



    public ContentDetailFragment() {
        // Required empty public constructor
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
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);
        //layoutinflater = inflater.inflate(R.layout.fragment_content_player, container, false);
        mImageButton = layoutInflater.findViewById(R.id.imgBanner);
        mButtonFavorite = layoutInflater.findViewById(R.id.button_favorite);
        mButtonDataBase = layoutInflater.findViewById(R.id.button_favorite2);
        //pressImage();
        article = (TextView) layoutInflater.findViewById(R.id.article_title);
        article.setText("hello");
        article_by_line = (TextView) layoutInflater.findViewById(R.id.article_byline);
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);

        setupViewPager(index);
        setupToggleDatabase(index);
        setupToggleFavorite(index);

        Log.d(TAG, "onCreateView: "+" count of detailfragments");

       /** getChildFragmentManager().beginTransaction()
                .replace(R.id.nested_container, ContentPlayerFragment.newInstance(mDownloadId, index))
                .commit();**/

        playButton = layoutInflater.findViewById(R.id.exo_play);
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
        mExoPlayerView = (SimpleExoPlayerView) layoutInflater.findViewById(R.id.exoplayer);

        initFullscreenDialog();
        initFullscreenButton();
        initExoPlayer();
        setupThumbNailSource();


        return layoutInflater;
    }

    public void onPlayerBackState() {
        if (lock != null) {
            if (mExoPlayerView.getPlayer().getPlayWhenReady())
                mExoPlayerView.getPlayer().setPlayWhenReady(false);
        }
    }

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
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
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
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
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
        ((FrameLayout) layoutInflater.findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
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
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
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









    private void setupToggleFavorite(final int index) {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        mButtonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                compoundButton.startAnimation(scaleAnimation);
                if (isChecked) {
                    final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.bottom_app_bar_coord), "ready to share..!", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addItemsToAppPreference(index);
                                    compoundButton.setChecked(false);
                                }
                            }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));


                    View view1 = snackbar.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view1.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view1.setLayoutParams(params);
                    view1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                    TextView snackBarText = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    snackBarText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                    snackbar.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            compoundButton.setChecked(false);
                        }
                    }, 3000);
                } else {

//                    ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
                    //                  meta.remove(index);
                    //                AppPreferences.setName(getContext(), meta);
                }
            }
        });
    }

    public void setupToggleDatabase(final int index) {
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
                                    addItemtsToDataBase(index);
                                    compoundButton.setChecked(false);
                                }
                            }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));


                    View view1 = snackbar.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view1.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view1.setLayoutParams(params);
                    view1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                    TextView snackBarText = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    snackBarText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
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

    public void addItemsToAppPreference(final int index) {
        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
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
                                    String url = (String) metadata.get("url");
                                    AppPreferences.clearNameList(getContext());
                                    ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
                                    meta.add(url + System.lineSeparator());
                                    AppPreferences.clearNameList(getContext());
                                    AppPreferences.setName(getContext(), meta);

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

    public void addItemtsToDataBase(final int pos) {
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                String output = getViewPagerContent(workInfo);
                                try {
                                    JSONArray input = new JSONArray(output);
                                    JSONObject container = input.getJSONObject(pos);
                                    JSONObject metadata = (JSONObject) container.get("metadata");
                                    String name = (String) metadata.get("name");
                                    String png = (String) metadata.get("png");
                                    String url = (String) metadata.get("url");
                                    Log.d(TAG, "onChanged: " + png);
                                    Log.d(TAG, "onChanged: " + url);
                                    Favourite favourite = new Favourite(png, url, 0);
                                    favouriteViewModel.insert(favourite);
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

    private void setupViewPager(final int index) {
        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
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
                                    String png = (String) metadata.get("png");
                                    //  Picasso.get().load(png).config(Bitmap.Config.RGB_565)
                                    //          .fit().centerCrop().into(mImageButton);
                                    article_by_line.setText(name);
                                    //mHeader.setText("uschi");
//                                    setupToggleFavorite(index);

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