package com.josef.mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.ViewGroup.getChildMeasureSpec;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.ONACTIVITYRESULTEXAMPLE;
import static com.josef.mobile.Config.ONVIEWPAGERINITLISTENER;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERHOMEFRAGMENTDETAILVAULE;
import static com.josef.mobile.Config.VIEWPAGERHOMEFRAGMENTMAINKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_VIEWPAGER;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link com.josef.mobile.MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private Data mData;
    private Constraints mConstraints;
    private OneTimeWorkRequest mDownload;
    public ImageView mImageButton;
    public ToggleButton buttonFavorite;
    public TextView article;
    public TextView article_by_line;
    public View layoutInflater;

    // TODO: Rename and change types of parameters

    private int which;
    private int index;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(int which,int index) {
        HomeFragment fragment = new HomeFragment();
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
        layoutInflater = inflater.inflate(R.layout.fragment_home, container, false);
        mImageButton = layoutInflater.findViewById(R.id.imgBanner);
        buttonFavorite = layoutInflater.findViewById(R.id.button_favorite);
        pressImage();
        article = (TextView)layoutInflater.findViewById(R.id.article_title);
        article.setText("hello");
        article_by_line = (TextView)layoutInflater.findViewById(R.id.article_byline);

        setupWorkRequest(which);
        executeWorkRequest();
        setupViewPager(index);

        return layoutInflater;
    }

    private void pressImage() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(VIEWPAGERMAINKEY,which);
                getActivity().startActivityForResult(intent, ONACTIVITYRESULTEXAMPLE);
            }
        });
    }

    private void setupToggleButton(final String url,final int index) {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                compoundButton.startAnimation(scaleAnimation);
                if (isChecked) {
                    Log.d(TAG, "onChanged: "+url);
                    ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
                    meta.add(url+System.lineSeparator());
                    AppPreferences.setName(getContext(),meta);
                } else {
                    ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(getContext()));
                    meta.remove(index);
                    AppPreferences.setName(getContext(),meta);
                }
            }
        });
    }
    public void shareMetaData(final int index){
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
                                    setupToggleButton(url,index);
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
                //Toast.makeText(getActivity(), state.toString(),
                // Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewPager(final int index ) {
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

                                    Picasso.get().load(png).config(Bitmap.Config.RGB_565)
                                            .fit().centerCrop().into(mImageButton);
                                    article_by_line.setText(name);
                                    //mHeader.setText("uschi");
                                    Log.d(TAG, "onChanged: "+name);

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