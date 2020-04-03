package com.josef.mobile.components;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.josef.mobile.MainActivityViewPagerFragment;


public class MainActivityViewPagerAdapter extends FragmentStateAdapter {


    public MainActivityViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MainActivityViewPagerFragment.newInstance("fdfd", "dfdfdf");
            case 1:
                return MainActivityViewPagerFragment.newInstance("ddfaaadf", "dfdfdf");
            case 2:
                return MainActivityViewPagerFragment.newInstance("ddfd7676f", "dfdfdf");

        }
        return null;
    }

}
