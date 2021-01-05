package com.josef.mobile.vfree.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;

import com.josef.mobile.vfree.SessionManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.AuthActivity;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.utils.UtilManager;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.vfree.ui.auth.AuthActivity.RC_SIGN_OUT;
import static com.josef.mobile.vfree.ui.err.ErrorActivity.ACTIVITY_KEYS;

public abstract class BaseActivity extends DaggerAppCompatActivity implements Base {

    @Inject
    public SessionManager sessionManager;

    @Inject
    public DataManager dataManager;

    @Inject
    public UtilManager utilManager;

    public AlertDialog.Builder alert;

    private Activity activity;

    private ConnectivityManager connectivityManager;

    private ConnectivityManager.NetworkCallback networkCallback;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new AlertDialog.Builder(this);

        connectivityManager = utilManager.getConnectivityManager();
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
            }

            @Override
            public void onLost(Network network) {
                if (getActivityName().equals(ERROR_ACTIVITY_NAME)) return;

                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(activity,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                Intent intent = new Intent(activity, ErrorActivity.class);
                intent.putExtra(ACTIVITY_KEYS, getActivityName());

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

    public void showProgressbar(Activity activity) {
        utilManager.showProgressbar(activity);
    }

    public void hideProgessbar() {
        utilManager.hideProgressbar();
    }

    public boolean isOnline() {
        return utilManager.isOnline();
    }

    private void navLoginScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, RC_SIGN_OUT);
        finish();
    }


}
















