package com.josef.mobile.free;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.test.espresso.IdlingResource;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.WORKREQUEST_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_VIEWPAGER;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Data mData;
    private Constraints mConstraints;
    private OneTimeWorkRequest mDownload;

    public PlayerView playerView;
    public TextView mHeader;
    public TextView mSubHeader;

    // TODO: Rename and change types of parameters
    private int which;
    private int index;



    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivityViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static DetailFragment newInstance(int param1, int param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(ARG_PARAM1);
            index = getArguments().getInt(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rooot = inflater.inflate(R.layout.fragment_detail, container, false);
        playerView = rooot.findViewById(R.id.video_view);
        mHeader = rooot.findViewById(R.id.article_title);
        mSubHeader = rooot.findViewById(R.id.article_byline);
        //EspressoIdlingResource.increment();
        mHeader.setText("uschi");
        setupWorkRequest(which);
        executeWorkRequest();
        setupViewPager(index);
        return rooot;

    }

    private void setupWorkRequest(final int index) {
        mData = buildData(index);
        mConstraints = buildConstraints();
        mDownload = buildOneTimeWorkRequest(mData, mConstraints);
    }

    private void executeWorkRequest() {
        WorkManager.getInstance(getActivity()).beginUniqueWork(WORKREQUEST_VIEWPAGER + mDownload.getId(),
                ExistingWorkPolicy.KEEP, mDownload).enqueue().getState().observe(this, new Observer<Operation.State>() {
            @Override
            public void onChanged(Operation.State state) {
                //Toast.makeText(getActivity(), state.toString(),
                // Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewPager(final int pos) {
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(mDownload.getId())
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
                                    //mHeader.setText("uschi");
                                    mSubHeader.setText(name);
                                    Log.d(TAG, "onChanged: "+name);
                                    String url = (String) metadata.get("url");
                                    initializePlayer(url);
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

    @NotNull
    private OneTimeWorkRequest buildOneTimeWorkRequest(Data data, Constraints constraints) {
        return new OneTimeWorkRequest.Builder(CallBackWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
    }

    @NotNull
    private Constraints buildConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

    @NotNull
    private Data buildData(int index) {
        return new Data.Builder()
                .putInt(WORKREQUEST_AMOUNT, index)
                .build();
    }

    private void initializePlayer(final String url) {
        //  https://medium.com/google-exoplayer/playing-ads-with-exoplayer-and-ima-868dfd767ea
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        Uri uri = Uri.parse(url);
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(getActivity(), "MyExoplayer"),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        player.prepare(mediaSource);
        playerView.setPlayer(player);
        player.setPlayWhenReady(false);
        playerView.setPlayer(player);

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

