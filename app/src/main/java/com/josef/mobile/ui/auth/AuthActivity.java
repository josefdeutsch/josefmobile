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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.josef.mobile.BaseActivity;
import com.josef.mobile.R;
import com.josef.mobile.ui.intro.AuthResource;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class AuthActivity extends BaseActivity implements
        View.OnClickListener {

    public static final int RC_SIGN_OUT = 9001;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    @Inject
    FirebaseAuth mAuth;
    @Inject
    GoogleSignInClient mGoogleSignInClient;
    // [END declare_auth]
    @Inject
    ViewModelProviderFactory providerFactory;
    private ConstraintLayout white, black;
    private AuthViewModel viewModel;

    private com.google.android.gms.common.SignInButton signInButton;

    private Button signOutButton, disconnectButton;
    private TextView status, detail;
    private LinearLayout signOutAndDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);

        white = findViewById(R.id.white);
        black = findViewById(R.id.black);
        //    mProgressBar = findViewById(R.id.progressBar);
        signInButton = findViewById(R.id.sign_in_button);
        signOutButton = findViewById(R.id.signOutButton);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        //   disconnectButton.setOnClickListener(this);
        viewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: ");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                //  logErrorMessage(e.getMessage());
                white.setVisibility(View.VISIBLE);
                black.setVisibility(View.GONE);
            }
        }
        if (requestCode == RC_SIGN_OUT) {
            signOut();
        }
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
        Log.d(TAG, "getGoogleAuthCredential: ");
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        //   displayProgressBar();
        viewModel.signInWithGoogle(googleAuthCredential);
        viewModel.getPosts().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }

                        case AUTHENTICATED: {
                            Log.d(TAG, "onChanged: PostsFragment: AUTHENTICATED... " + userAuthResource.data.email);
                            break;
                        }

                        case ERROR: {
                            break;
                        }
                        case NOT_AUTHENTICATED: {
                            break;
                        }
                    }
                }
            }
        });
    }

    private void signIn() {
        white.setVisibility(View.GONE);
        black.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "signIn: ");
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //                    updateUI(null);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.signOutButton) {
            signOut();
        }
        /**else if (i == R.id.disconnectButton) {
         revokeAccess();
         }**/
    }
}