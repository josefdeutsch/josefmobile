package com.josef.mobile;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorker;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;


import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_VIEWPAGER;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View mRootView;
    private Data mData;
    private Constraints mConstraints;
    private OneTimeWorkRequest mDownload;

    private Handler mainHandler;
    private LayoutInflater mLayoutInflater;
    private Integer mAdapterPosition;
    private LinearLayout mLayout;
    // TODO: Rename and change types of parameters
    private int amountOfViewpager;


    public MainFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param amount Parameter 1
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(int amount) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(VIEWPAGER_AMOUNT, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amountOfViewpager = getArguments().getInt(VIEWPAGER_AMOUNT,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_main, container, false);
        mLayoutInflater = LayoutInflater.from(getActivity());
        mLayout = layoutInflater.findViewById(R.id.container);
        EspressoIdlingResource.increment();

        for (int index = 0; index <= amountOfViewpager; index++) {
            ViewPager2 child = (ViewPager2) mLayoutInflater.inflate(R.layout.viewpager, null);
            final MainActivityViewPagerAdapter myAdapter= new MainActivityViewPagerAdapter(getActivity(), null);
            child.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            child.setAdapter(myAdapter);
            child.setOffscreenPageLimit(3);
            mLayout.addView(child);
            final float pageMargin = getActivity().getResources().getDimensionPixelOffset(R.dimen.pageMargin);
            final float pageOffset = getActivity().getResources().getDimensionPixelOffset(R.dimen.offset);

            //https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419
            child.setPageTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float myOffset = position * -(2 * pageOffset + pageMargin);
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    if (position < -1) {
                        page.setTranslationX(-myOffset);
                       // page.setAlpha(scaleFactor);

                    } else if (position <= 1) {
                        page.setTranslationX(myOffset);
                        page.setScaleY(scaleFactor);

                    } else {
                        //page.setAlpha(scaleFactor);
                        page.setTranslationX(myOffset);

                    }
                }
            });
            setupWorkRequest(index);
            executeWorkRequest();
            setupViewPager(myAdapter);
        }
        return layoutInflater;
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

    private void setupViewPager(final MainActivityViewPagerAdapter myAdapter) {
        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(mDownload.getId())
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                ArrayList<String> arrayList = getViewPagerContent(workInfo);
                                myAdapter.setmValues(arrayList);

                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                     EspressoIdlingResource.decrement();
                                }
                            }
                        }
                    }
                });
    }

    @Nullable
    private ArrayList<String> getViewPagerContent(@NotNull WorkInfo workInfo) {
        Gson gson = new Gson();
        Data data = workInfo.getOutputData();
        String output = data.getString(KEY_TASK_OUTPUT);
        Type token = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(output, token);
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
    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
