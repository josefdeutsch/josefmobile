package com.josef.mobile.free.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.josef.josefmobile.R;
import com.josef.mobile.AppPreferences;
import com.josef.mobile.InterstitialAdsRequest;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static com.josef.mobile.Config.WORKREQUEST_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_VIEWPAGER;


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
    private Data mData;
    private Constraints mConstraints;
    private OneTimeWorkRequest mDownload;
    public ImageView mImageButton;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
    public TextView article;
    public TextView article_by_line;
    public View layoutInflater;

    // TODO: Rename and change types of parameters

    private int which;
    private int index;


    public ContentDetailFragment() {
        // Required empty public constructor
    }

    public static ContentDetailFragment newInstance(int which, int index) {
        ContentDetailFragment fragment = new ContentDetailFragment();
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY, which);
        args.putInt(VIEWPAGERDETAILKEY, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(VIEWPAGERMAINKEY);
            index = getArguments().getInt(VIEWPAGERDETAILKEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);
        mImageButton = layoutInflater.findViewById(R.id.imgBanner);
        mButtonFavorite = layoutInflater.findViewById(R.id.button_favorite);
        mButtonDataBase = layoutInflater.findViewById(R.id.button_favorite2);
        //pressImage();
        article = (TextView) layoutInflater.findViewById(R.id.article_title);
        article.setText("hello");
        article_by_line = (TextView) layoutInflater.findViewById(R.id.article_byline);
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);

        setupWorkRequest(which);
        executeWorkRequest();
        setupViewPager(index);

        FragmentTransaction fm = getChildFragmentManager().beginTransaction();
        getChildFragmentManager().beginTransaction()
                .add(R.id.nested_container, ContentPlayerFragment.newInstance(mDownload.getId().toString(),index))
                .commit();
        fm.commit();


        //intent = new Intent(getActivity(), DetailActivity.class);
        //intent.putExtra(VIEWPAGERMAINKEY, which);
        //intent.putExtra(VIEWPAGERDETAILKEY, index);

        return layoutInflater;
    }

    public void onPlayBackState(){
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.nested_container);
        if(fragment instanceof ContentPlayerFragment){
            ContentPlayerFragment playerFragment =(ContentPlayerFragment)fragment;
            playerFragment.onPlayerBackState();
        }
    }

    public void setupMediaSource(){
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.nested_container);
        if(fragment instanceof ContentPlayerFragment){
            ContentPlayerFragment playerFragment =(ContentPlayerFragment)fragment;
            playerFragment.setupMediaSource();
        }
    }

    Intent intent;

    private void pressImage() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupProgressBar();
                loadIntersitialAds(new InterstitialAdsRequest() {
                    @Override
                    public void execute() {
                        mInterstitialAd = new InterstitialAd(getContext());
                        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                if (mDialog != null) {
                                    mDialog.hide();
                                }
                                mInterstitialAd.show();
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                if (mDialog != null) {
                                    mDialog.hide();
                                }
                                getActivity().startActivity(intent);
                            }

                            @Override
                            public void onAdClosed() {
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

    private InterstitialAd mInterstitialAd;

    private AlertDialog mDialog;

    private void setupProgressBar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progressdialog);
        mDialog = builder.create();
        mDialog.show();
    }

    public void loadIntersitialAds(InterstitialAdsRequest request) {
        request.execute();
    }

    private void setupToggleFavorite(final String url, final int index) {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        mButtonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                compoundButton.startAnimation(scaleAnimation);
                if (isChecked) {
                    Log.d(TAG, "onChanged: " + url);
                    ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
                    meta.add(url + System.lineSeparator());
                    AppPreferences.setName(getContext(), meta);
                } else {
                    ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
                    meta.remove(index);
                    AppPreferences.setName(getContext(), meta);
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
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                compoundButton.startAnimation(scaleAnimation);
                if (isChecked) {
                   addItemtsToDataBase(index);
                } else {
                   //removeItemtsFromDataBase(index);
                }
            }
        });
    }

    public void addItemsToAppPreference(final int index) {
        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(mDownload.getId())
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
                                    setupToggleFavorite(url, index);
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


    public void addItemtsToDataBase(final int pos){
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
                                    String png = (String) metadata.get("png");
                                    String url = (String) metadata.get("url");
                                    Log.d(TAG, "onChanged: "+png);
                                    Log.d(TAG, "onChanged: "+url);
                                    Favourite favourite = new Favourite(png,url,0);

                                    favouriteViewModel.insert(favourite);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                            }
                        }
                    }
                });
    }

    private void setupViewPager(final int index) {
        EspressoIdlingResource.increment();
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(mDownload.getId())
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
                                    Log.d(TAG, "onChanged: " + name);

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
    private void setupWorkRequest(int index) {
        mData = buildData(index);
        mConstraints = buildConstraints();
        mDownload = buildOneTimeWorkRequest(mData, mConstraints);
    }

    private void executeWorkRequest() {
        WorkManager.getInstance(getActivity()).beginUniqueWork(WORKREQUEST_VIEWPAGER + mDownload.getId(),
                ExistingWorkPolicy.KEEP, mDownload).enqueue().getState().observe(this, new Observer<Operation.State>() {
            @Override
            public void onChanged(Operation.State state) {

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