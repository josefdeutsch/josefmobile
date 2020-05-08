package com.josef.mobile.free.ui;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkManager;

import com.josef.josefmobile.R;
import com.josef.mobile.free.DetailFragment;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorker;

import org.jetbrains.annotations.NotNull;

import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static com.josef.mobile.Config.WORKREQUEST_AMOUNT;
import static com.josef.mobile.Config.WORKREQUEST_VIEWPAGER;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContentContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ContentContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private int which;
    ViewPager viewPager;
    private Integer mPosition;
    private ToggleButton buttonFavorite;
    View layoutInflater;
    ContentDetailFragment mHomeFragment;
    private Data mData;
    private Constraints mConstraints;
    private OneTimeWorkRequest mDownload;

    // erstes Item wird nicht initialisiert - rekursiv.

    public ContentContainerFragment() {

    }

    @Nullable
    public static ContentContainerFragment newInstance(int which) {
        ContentContainerFragment fragment = new ContentContainerFragment();
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY, which);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getInt(VIEWPAGERMAINKEY);
        }
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(VIEWPAGERDETAILKEY, 0);
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

    ViewPagerFragmentAdapters adapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setupWorkRequest(which);
        executeWorkRequest();

        layoutInflater = inflater.inflate(R.layout.fragment_content_container, container, false);
        viewPager = layoutInflater.findViewById(R.id.viewidpager);
        adapters = new ViewPagerFragmentAdapters(getChildFragmentManager(), mDownload.getStringId());
        //EspressoIdlingResource.increment();
        viewPager.setAdapter(adapters);
        viewPager.setOffscreenPageLimit(1);
        //zu schnell background thread problematisch..
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mHomeFragment = (ContentDetailFragment) adapters.getRegisteredFragment(position);
                SparseArray<Fragment> array = adapters.getRegisteredFragments();
                int len = array.size();
                for (int i = 0; i <= len-1 ; i++) {
                    ContentDetailFragment detailFragment = (ContentDetailFragment) array.get(i);
                    if(i != position) detailFragment.onPlayBackState();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //viewPager.setCurrentItem(0);
        //mHomeFragment.addItemtsToDataBase(0);

        //viewPager.setCurrentItem(mPosition);
        return layoutInflater;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        viewPager.setAdapter(null);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(VIEWPAGERDETAILKEY, mPosition);
    }

    public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

        public String mDownloadId;
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ViewPagerFragmentAdapters(FragmentManager fm, String id) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mDownloadId = id;
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        public SparseArray<Fragment> getRegisteredFragments() {
            return registeredFragments;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Fragment getItem(int position) {
            if (registeredFragments.get(position) != null) return registeredFragments.get(position);
            return ContentDetailFragment.newInstance(mDownloadId, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
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
