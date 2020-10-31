package com.josef.mobile.ui.base;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.josef.mobile.SessionManager;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.utils.CommonUtils;
import com.josef.mobile.utils.net.NetworkUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.auth.AuthActivity.RC_SIGN_OUT;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "DaggerExample";

    @Inject
    public SessionManager sessionManager;

    private final BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (NetworkUtils.isNetworkAvailable(context)) {
            } else {
                Toast.makeText(context, "Network not available", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObservers();
    }

    @Inject
    public CommonUtils commonUtils;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private void subscribeObservers() {
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
                            Log.d(TAG, "onChanged: BaseActivity: AUTHENTICATED... " +
                                    "Authenticated as: " + userAuthResource.data.getUid() +
                                    " " + userAuthResource.data.getId());
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

}
















