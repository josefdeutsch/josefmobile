package com.josef.mobile.free.ui.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.josef.mobile.free.ui.detail.ContentDetailFragment;

public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

    private String downloadid;
    private int query;

    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ViewPagerFragmentAdapters(FragmentManager fm, final String downloadid, final int query) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.downloadid = downloadid;
        this.query = query;

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
        return ContentDetailFragment.newInstance(downloadid, position, query);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }
}
