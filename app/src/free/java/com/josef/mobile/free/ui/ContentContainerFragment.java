package com.josef.mobile.free.ui;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private static final String ARG_PARAM1 = "param1";
    private String which;
    ViewPager viewPager;
    private int mPosition;
    View layoutInflater;
    ContentDetailFragment mHomeFragment;

    public ContentContainerFragment() {

    }

    @Nullable
    public static ContentContainerFragment newInstance(String which) {
        ContentContainerFragment fragment = new ContentContainerFragment();
        Bundle args = new Bundle();
        args.putString(VIEWPAGERMAINKEY, which);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getString(VIEWPAGERMAINKEY);
        }
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(VIEWPAGERDETAILKEY, 0);
    }


    ViewPagerFragmentAdapters adapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater = inflater.inflate(R.layout.fragment_content_container, container, false);
        viewPager = layoutInflater.findViewById(R.id.viewidpager);
        adapters = new ViewPagerFragmentAdapters(getChildFragmentManager(), which);
        viewPager.setAdapter(adapters);
        viewPager.setOffscreenPageLimit(3);
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
                for (int i = 0; i <= len - 1; i++) {
                    if ((ContentDetailFragment) array.get(i) != null) {
                        ContentDetailFragment detailFragment = (ContentDetailFragment) array.get(i);
                        if (i != position) detailFragment.onPlayerBackState();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mPosition);

        return layoutInflater;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewPager.setAdapter(null);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(VIEWPAGERDETAILKEY, mPosition);
    }

    public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

        public String downloadid;
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ViewPagerFragmentAdapters(FragmentManager fm, final String id) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            downloadid = id;
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
            return ContentDetailFragment.newInstance(downloadid, position);
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
