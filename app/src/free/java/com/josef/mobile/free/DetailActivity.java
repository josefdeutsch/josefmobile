package com.josef.mobile.free;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.net.Uri;
import android.os.Bundle;
import com.josef.josefmobile.R;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {

    private ViewPagerFragmentAdapter mAdapter;
    private ViewPager2 mViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupAdapter();
        registerListener();
    }

    private void registerListener() {
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
    }

    private void setupAdapter() {
        mViewPager2 = findViewById(R.id.viewPager);
        mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager2.setAdapter(mAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class ViewPagerFragmentAdapter extends FragmentStateAdapter {


        public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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
                    return DetailFragment.newInstance("fdfd", "dfdfdf");
                case 1:
                    return DetailFragment.newInstance("ddfaaadf", "dfdfdf");
                case 2:
                    return DetailFragment.newInstance("ddfd7676f", "dfdfdf");

            }
            return null;
        }

    }
}
