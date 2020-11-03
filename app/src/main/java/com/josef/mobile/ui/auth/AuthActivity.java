package com.josef.mobile.ui.auth;


/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.R;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class AuthActivity extends DaggerAppCompatActivity implements
        View.OnClickListener {

    public static final int RC_SIGN_OUT = 9001;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    @Inject
    FirebaseAuth mAuth;
    @Inject
    GoogleSignInClient mGoogleSignInClient;
    @Inject
    ViewModelProviderFactory providerFactory;
    private AuthViewModel viewModel;


    private com.google.android.gms.common.SignInButton signInButton;

    Task<GoogleSignInAccount> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout2);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        subscribeObserver();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (task != null && task.isSuccessful())
            startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: ");
            task = GoogleSignIn.getSignedInAccountFromIntent(data);
            viewModel.signInWithGoogle(task);
        }
        if (requestCode == RC_SIGN_OUT) {
            signOut();
        }
    }

    public void subscribeObserver() {

        viewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);
        viewModel.getPosts().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case LOADING: {
                            findViewById(R.id.progressauth).setVisibility(View.VISIBLE);
                            Log.d(TAG, "onChanged: AuthActivity: LOADING...");
                            break;
                        }

                        case AUTHENTICATED: {
                            findViewById(R.id.progressauth).setVisibility(View.GONE);
                            Log.d(TAG, "onChanged: AuthActivity: AUTHENTICATED... " +
                                    "Authenticated as: " + userAuthResource.data.getEmail());
                            break;
                        }

                        case ERROR: {
                            findViewById(R.id.progressauth).setVisibility(View.GONE);
                            Log.d(TAG, "onChanged: AuthActivity: ERROR...");
                            break;
                        }

                        case NOT_AUTHENTICATED: {
                            findViewById(R.id.progressauth).setVisibility(View.GONE);
                            Log.d(TAG, "onChanged: AuthActivity: NOT AUTHENTICATED. Navigating to Login screen.");
                            break;
                        }
                    }
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }
}