package com.josef.josefmobile.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.josef.josefmobile.ui.base.BaseFragment;
import com.josef.josefmobile.ui.err.ErrorActivity;
import com.josef.josefmobile.ui.main.MainActivity;
import com.josef.mobile.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.josef.josefmobile.ui.err.ErrorActivity.ACTIVITY_KEYS;

public class ProfileFragment extends BaseFragment implements ViewPagerAdapter.ViewpagerAdapterOnClickListener {

    @Inject
    ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.viewPager2)
    ViewPager2 viewPager2;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private ProfileViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);

        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager2.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setViewpagerAdapterOnClickListener(this);

        viewModel = new ViewModelProvider(this, providerFactory).get(ProfileViewModel.class);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

        }).attach();

        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observeProfiles().removeObservers(getViewLifecycleOwner());
        viewModel.observeProfiles().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        showProgressbar(getActivity());
                        break;
                    }
                    case SUCCESS: {
                        viewPagerAdapter.setProfiles(listResource.data);
                        hideProgessbar();
                        break;
                    }
                    case ERROR: {
                        hideProgessbar();
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getActivity(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        Intent intent = new Intent(getActivity(), ErrorActivity.class);
                        intent.putExtra(ACTIVITY_KEYS, getActivity().getComponentName().getClassName());

                        startActivity(intent, bundle);
                        getActivity().finishAfterTransition();
                        break;
                    }
                }
            }
        });
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
