package com.josef.mobile.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.josef.mobile.R;
import com.josef.mobile.ui.base.BaseFragment;
import com.josef.mobile.ui.main.MainActivity;

import java.util.Arrays;

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
        viewPager2 = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager2.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setViewpagerAdapterOnClickListener(this);
        viewPagerAdapter.setArrayList(
                Arrays.asList(this.getResources().getStringArray(R.array.profile_text_supplier)),
                Arrays.asList(this.getResources().getStringArray(R.array.profile_url_supplier))
        );

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

        }).attach();
    }


    @Override
    public void onItemInfoClicked() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_info);
        ((MainActivity) getActivity()).onNavigationItemSelected(menuItem);
    }

    @Override
    public void onItemContinueClicked() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_posts);
        ((MainActivity) getActivity()).onNavigationItemSelected(menuItem);
    }
}
