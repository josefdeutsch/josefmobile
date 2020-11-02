package com.josef.mobile.ui.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
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
        subscribeObservers();
        activity = this;

        //  Dialog dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // dialog.setContentView(R.layout.nowifi_dialog);

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(this);
        connectionLiveData.removeObservers(this);
        connectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                Dialog dialog = utilManager.getNoInternetConnection(activity);
                dialog.setContentView(R.layout.nowifi_dialog);

                Log.d(TAG, "onChanged: " + dialog.toString());
                if (aBoolean) {
                    Log.d(TAG, "onChanged: " + aBoolean);
                    dialog.hide();
                    dialog.dismiss();
                } else {
                    Log.d(TAG, "onChanged: " + aBoolean);
                    dialog.show();
                }
            }
        });

    }

    @Inject
    public UtilManager utilManager;


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

    @Override
    protected void onStop() {
        super.onStop();
        Dialog dialog = utilManager.getNoInternetConnection(activity);
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
















