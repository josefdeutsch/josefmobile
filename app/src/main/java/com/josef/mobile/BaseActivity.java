package com.josef.mobile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.googlesignin.GoogleSignInActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.googlesignin.GoogleSignInActivity.RC_SIGN_OUT;


public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "DaggerExample";

    @Inject
    public SessionManager sessionManager;

    @Inject
    public FirebaseManager firebaseManager;

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
        firebaseManager.getAuthUser().observe(this, new Observer<AuthResource<FirebaseUser>>() {
            @Override
            public void onChanged(AuthResource<FirebaseUser> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {

                        case LOADING: {
                            Log.d(TAG, "onChanged: BaseActivity: LOADING...");
                            break;
                        }

                        case AUTHENTICATED: {
                            Log.d(TAG, "onChanged: BaseActivity: AUTHENTICATED... " +
                                    "Authenticated as: " + userAuthResource.data.getEmail());
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
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_OUT);
        finish();
    }
}
















