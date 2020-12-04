package com.josef.mobile.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.josef.mobile.R;
import com.josef.mobile.SessionManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.err.ErrorActivity;
import com.josef.mobile.utils.UtilManager;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.auth.AuthActivity.RC_SIGN_OUT;
import static com.josef.mobile.ui.err.ErrorActivity.ACTIVITY_KEY;
import static com.josef.mobile.ui.err.ErrorActivity.ERROR_ACTIVITY;
import static com.josef.mobile.ui.err.ErrorActivity.EXECEPTION_KEY;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity";

    Activity activity;

    @Inject
    public SessionManager sessionManager;

    @Inject
    public DataManager dataManager;

    @Inject
    public UtilManager utilManager;

    ConnectivityManager connectivityManager;

    ConnectivityManager.NetworkCallback networkCallback;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = getSystemService(ConnectivityManager.class);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
            }

            @Override
            public void onLost(Network network) {
                if (getActivityName().equals(ERROR_ACTIVITY)) return;
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(activity,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                Intent intent = new Intent(activity, ErrorActivity.class);
                intent.putExtra(ACTIVITY_KEY, getActivityName());
                intent.putExtra(EXECEPTION_KEY, getApplicationContext().getResources().getString(R.string.activity_error_nonnetwork));

                startActivity(intent, bundle);

                finishAfterTransition();
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            }

        };
        connectivityManager.registerDefaultNetworkCallback(networkCallback);

        subscribeToSessionManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public String getActivityName() {
        activity = this;
        return activity.getComponentName().getClassName();
    }

    public void subscribeToSessionManager() {
        if (sessionManager.getAuthUser() == null) return;
        sessionManager.getAuthUser().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case NOT_AUTHENTICATED: {
                        navLoginScreen();
                        break;
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


}
















