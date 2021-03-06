package com.josef.mobile.vfree.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.player.PlayerActivity;
import com.josef.mobile.vfree.utils.AppConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.josef.mobile.vfree.ui.base.Base.REQUEST_ENDPOINT;
import static com.josef.mobile.vfree.ui.base.Base.REQUEST_INDEX;
import static com.josef.mobile.vfree.ui.err.ErrorActivity.ACTIVITY_KEYS;

public final  class HomeFragment extends BaseFragment
        implements ViewPagerAdapter.ViewpagerAdapterOnClickListener {

    @Inject
    ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.viewPager2)
    ViewPager2 viewPager2;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private HomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

        viewModel = new ViewModelProvider(this, providerFactory).get(HomeViewModel.class);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

        }).attach();

        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observeProfiles(AppConstants.ENDPOINT_3).removeObservers(getViewLifecycleOwner());
        viewModel.observeProfiles(AppConstants.ENDPOINT_3).observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        Log.d(TAG, "subscribeObservers: HOME");
                        showProgressbar(getActivity());
                        break;
                    }
                    case SUCCESS: {
                        if(listResource.data != null && !listResource.data.isEmpty()){
                            viewPagerAdapter.setProfiles(listResource.data);
                        }
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

    @Override
    public void onItemArrowClicked(int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(REQUEST_INDEX, position);
        intent.putExtra(REQUEST_ENDPOINT, AppConstants.ENDPOINT_3);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
