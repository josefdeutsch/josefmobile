package com.josef.mobile.free.ui.container;

import android.app.Activity;
import android.content.Intent;
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
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;

import com.josef.josefmobile.R;
import com.josef.mobile.free.ui.adapter.ViewPagerFragmentAdapters;
import com.josef.mobile.free.ui.detail.ContentDetailFragment;
import com.josef.mobile.idlingres.EspressoIdlingResource;


import static com.josef.mobile.free.ui.detail.ViewModelDetail.QUERY_PARAM;
import static com.josef.mobile.ui.ErrorActivity.TAG;
import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class ContentContainerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String which;
    private int query;
    private ViewPager viewPager;
    private int mPosition;
    private View layoutInflater;
    private ViewPagerFragmentAdapters adapters;
    protected static final int DIALOG_FRAGMENT = 1;

    private ContentDetailFragment mHomeFragment;

    //

    public ContentContainerFragment() {

    }

    @Nullable
    public static ContentContainerFragment newInstance(String which, int query) {
        ContentContainerFragment fragment = new ContentContainerFragment();
        Bundle args = new Bundle();
        args.putString(WORKREQUEST_DOWNLOADID, which);
        args.putInt(QUERY_PARAM, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            which = getArguments().getString(WORKREQUEST_DOWNLOADID);
            query = getArguments().getInt(QUERY_PARAM);
        }
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(VIEWPAGERDETAILKEY, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater = inflater.inflate(R.layout.fragment_content_container, container, false);
        viewPager = layoutInflater.findViewById(R.id.viewidpager);
        adapters = new ViewPagerFragmentAdapters(getChildFragmentManager(), which, query);
        viewPager.setAdapter(adapters);

        viewPager.setOffscreenPageLimit(1);
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
