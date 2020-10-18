package com.josef.mobile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.User;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.auth.AuthActivity.RC_SIGN_OUT;


public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "DaggerExample";


    @Inject
    public SessionManager sessionManager;

    @Inject
    FirebaseAuth authApi;

    @Inject
    GoogleSignInClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObservers();
    }

    private void subscribeObservers() {
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {

                        case LOADING: {

                            // white.setVisibility(View.GONE);
                            // black.setVisibility(View.VISIBLE);

                            Log.d(TAG, "onChanged: BaseActivity: LOADING...");
                            break;
                        }

                        case AUTHENTICATED: {

                            // white.setVisibility(View.VISIBLE);
                            // black.setVisibility(View.GONE);
                            // navMainScreen();

                            Log.d(TAG, "onChanged: BaseActivity: AUTHENTICATED... " +
                                    "Authenticated as: " + userAuthResource.data.getEmail());
                            break;
                        }

                        case ERROR: {

                            // white.setVisibility(View.VISIBLE);
                            // black.setVisibility(View.GONE);

                            Log.d(TAG, "onChanged: BaseActivity: ERROR...");
                            break;
                        }

                        case NOT_AUTHENTICATED: {

                            //  white.setVisibility(View.VISIBLE);
                            //  black.setVisibility(View.GONE);

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
















