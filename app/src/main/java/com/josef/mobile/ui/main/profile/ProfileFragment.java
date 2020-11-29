package com.josef.mobile.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.josef.mobile.R;
import com.josef.mobile.ui.base.BaseFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment implements ViewPagerAdapter.ViewpagerAdapterOnClickListener {

    private static final String TAG = "ProfileFragment";
    ViewPager2 viewPager2;

    TabLayout tabLayout;

    @Inject
    ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> strings = new ArrayList<>();

        strings.add("");
        strings.add("Digital art is true");
        strings.add("Choose artworks");
        strings.add("Sync account");
        strings.add("Press play!");

        ArrayList<String> uri = new ArrayList<>();
        uri.add("http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif");

        uri.add("http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif");
        uri.add("http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif");
        uri.add("http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif");
        uri.add("http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif");

        viewPager2 = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager2.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setViewpagerAdapterOnClickListener(this);
        viewPagerAdapter.setArrayList(strings, uri);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

        }).attach();
    }


    @Override
    public void onItemInfoClicked() {

    }

    @Override
    public void onItemContinueClicked() {

    }
}
