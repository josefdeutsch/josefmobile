package com.josef.mobile.ui.base;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.josef.mobile.R;
import com.josef.mobile.SessionManager;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.main.ConnectionLiveData;
import com.josef.mobile.utils.UtilManager;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.auth.AuthActivity.RC_SIGN_OUT;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "DaggerExample";

    Activity activity;

    @Inject
    public SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setTransparentStatusBarLollipop();
        // setTransparentStatusBarMarshmallow();
        subscribeToSessionManager();
        activity = this;
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(this);
        connectionLiveData.removeObservers(this);
        connectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    utilManager.hideNoInternetConnection();
                } else {
                    utilManager.showNoInternetConnection(activity);
                }
            }
        });

    }

    @Inject
    public UtilManager utilManager;


    public void subscribeToSessionManager() {
        if (sessionManager.getAuthUser() == null) return;
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: BaseActivity: LOADING...");
                            break;
                        }
                        case AUTHENTICATED: {
                            Log.d(TAG, "onChanged: BaseActivity: AUTHENTICATED... ");

                            break;
                        }
                        case ERROR: {
                            Log.d(TAG, "onChanged: BaseActivity: ERROR...");
                            break;
                        }
                        case NOT_AUTHENTICATED: {
                            Log.d(TAG, "onChanged: BaseActivity: NOT AUTHENTICATED. Navigating to Login screen.");
                            navLoginScreen();
                            break;
                        }
                    }
                }
            }
        });
    }


    private void navLoginScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, RC_SIGN_OUT);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setTransparentStatusBarLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            setTransparentStatusBarMarshmallow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setTransparentStatusBarMarshmallow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void setNavigationBarSafeArea(@NonNull View view, @NonNull Activity activity) {

        // Check whether the app is in multi-mode or not
        boolean isInMultiWindowMode = activity.isInMultiWindowMode();

        if (!isInMultiWindowMode) {
            // Only when app is not in multi-mode
            Context context = this.getBaseContext();

            int showNavigationBarInt = context.getResources()
                    .getIdentifier("config_showNavigationBar", "bool", "android");

            // Check if the navigation bar is showing or not.
            // This would be true only if the application is showing the navigation bar in translucent mode
            boolean showNavigationBar = showNavigationBarInt > 0 && context.getResources().getBoolean(showNavigationBarInt);

            // This is specific for the emulator since it always gives as false for showNavigationBar
            // Especially for Google Devices
            if (Build.FINGERPRINT.contains("generic"))
                showNavigationBar = true;

            if (showNavigationBar) {
                // Once we know navigation bar is showing we need to identify the height of navigation bar
                int navigationBarHeight = context.getResources()
                        .getIdentifier("navigation_bar_height", "dimen", "android");

                if (navigationBarHeight > 0) {
                    navigationBarHeight = context.getResources().getDimensionPixelSize(navigationBarHeight);
                }

                // Apply the height of navigation bar as a padding to the view which is the bottom-most view in your screen
                // Very important to do this in onCreate of the view else it would end up applying more padding from bottom
                view.setPaddingRelative(view.getPaddingStart(),
                        view.getPaddingTop(),
                        view.getPaddingEnd(),
                        view.getPaddingBottom() + navigationBarHeight);
            } else {
                // Ignore since navigation bar is not showing in translucent mode
            }
        } else {
            // Ignore since the application is in multi-mode
        }
    }


}
















