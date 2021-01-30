package com.josef.mobile.vfree.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.navigation.NavigationView;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseActivity;
import com.josef.mobile.vfree.data.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LifecycleOwner {

    @NonNull
    private DrawerLayout drawerLayout;

    @NonNull
    private NavigationView navigationView;

    @NonNull
    @Inject
    ViewModelProviderFactory providerFactory;

    @NonNull
    private MainViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View headerLayout = navigationView.inflateHeaderView(R.layout.navigation_header);

        TextView firstName = headerLayout.findViewById(R.id.nav_name);
        TextView lastName = headerLayout.findViewById(R.id.nav_email);

        init();

        viewModel = new ViewModelProvider(this, providerFactory).get(MainViewModel.class);
        viewModel.getDataStoreCredentials().removeObservers(this);
        viewModel.observeDataStoreCredentials(this).observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                if (userResource != null) {
                    switch (userResource.status) {
                        case LOADING: {
                            break;
                        }
                        case SUCCESS: {
                            firstName.setText(userResource.data.fname + " " + userResource.data.lname);
                            lastName.setText(userResource.data.email);
                            break;
                        }
                        case ERROR: {
                            Toast.makeText(MainActivity.this, userResource.message, Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataManager.clearHashmapIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }

    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> setToolbarColor());
    }

    private void setToolbarColor() {
        getSupportActionBar().setTitle("");
        getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_home: {
                // nav options to clear backstack
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.main, true)
                        .build();

                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.homeScreen,
                                null,
                                navOptions
                        );
                break;
            }

            case R.id.nav_posts: {
                if (isValidDestination(R.id.nav_posts)) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                            .navigate(R.id.postScreen);
                    viewModel.initiateInsterstitialAds(new OnAdsInstantiated() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(LoadAdError adError) {
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                                    .navigate(R.id.homeScreen);
                        }

                        @Override
                        public void onAdClicked() {

                        }
                    });
                }
                break;
            }

            case R.id.nav_archive: {
                if (isValidDestination(R.id.archiveScreen)) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                            .navigate(R.id.archiveScreen);
                }
                break;
            }

            case R.id.nav_tv: {
                if (isValidDestination(R.id.nav_tv)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.tvScreen);
                }
                break;
            }
            case R.id.nav_info: {
                if (isValidDestination(R.id.infoScreen)) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.infoScreen);

                }
                break;
            }
            case R.id.nav_events: {
                if (isValidDestination(R.id.eventScreen)) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.eventScreen);

                }
                break;
            }
            case R.id.nav_profiles: {
                if (isValidDestination(R.id.profilesScreen)) {
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.profilesScreen);

                }
                break;
            }

        }
        menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public boolean isValidDestination(int destination) {
        return destination != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout: {
                sessionManager.logOut();
                return true;
            }

            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    return false;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            sessionManager.logOut();
        }
    }
}


















