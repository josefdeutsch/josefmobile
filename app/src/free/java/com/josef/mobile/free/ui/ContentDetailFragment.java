package com.josef.mobile.free.ui;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.AppPreferences;
import com.josef.mobile.InterstitialAdsRequest;
import com.josef.mobile.OnPlayExecute;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static com.josef.mobile.Config.WORKERDOWNLOADID;
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
    public ImageView mImageButton;
    public ToggleButton mButtonFavorite;
    public ToggleButton mButtonDataBase;
    public TextView article;
    public TextView article_by_line;
    public View layoutInflater;

    // TODO: Rename and change types of parameters

    private String mDownloadId;
    private int index;


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


        setupViewPager(index);
        // setupToggleFavorite(index);
        setupToggleDatabase(index);
        setupToggleFavorite(index);

        FragmentTransaction fm = getChildFragmentManager().beginTransaction();
        getChildFragmentManager().beginTransaction()
                .add(R.id.nested_container, ContentPlayerFragment.newInstance(mDownloadId, index))
                .commit();
        fm.commit();

        Log.d(TAG, "onCreateView: "+" count of detailFragments");

        //intent = new Intent(getActivity(), DetailActivity.class);
        //intent.putExtra(VIEWPAGERMAINKEY, which);
        //intent.putExtra(VIEWPAGERDETAILKEY, index);

        return layoutInflater;
    }


    public void onPlayBackState() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.nested_container);
        if (fragment instanceof ContentPlayerFragment) {
            ContentPlayerFragment playerFragment = (ContentPlayerFragment) fragment;
            playerFragment.onPlayerBackState();
        }
    }


    public void onPlayExecute() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.nested_container);
        if (fragment instanceof ContentPlayerFragment) {
            final ContentPlayerFragment playerFragment = (ContentPlayerFragment) fragment;
            playerFragment.onPlayExecute();
        }
    }


    public void setupMediaSource() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.nested_container);
        if (fragment instanceof ContentPlayerFragment) {
            ContentPlayerFragment playerFragment = (ContentPlayerFragment) fragment;
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

    /**
     * private void setupToggleFavorite(final String url, final int index) {
     * final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
     * scaleAnimation.setDuration(500);
     * BounceInterpolator bounceInterpolator = new BounceInterpolator();
     * scaleAnimation.setInterpolator(bounceInterpolator);
     * <p>
     * mButtonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
     *
     * @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
     * compoundButton.startAnimation(scaleAnimation);
     * if (isChecked) {
     * Log.d(TAG, "onChanged: " + url);
     * ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
     * meta.add(url + System.lineSeparator());
     * AppPreferences.setName(getContext(), meta);
     * } else {
     * ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
     * meta.remove(index);
     * AppPreferences.setName(getContext(), meta);
     * }
     * }
     * });
     * }
     ***/
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