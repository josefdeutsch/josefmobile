package com.josef.mobile.ui.googlesignin;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.josef.mobile.R;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements
        View.OnClickListener {

    public static final int RC_SIGN_OUT = 9001;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    @Inject
    FirebaseAuth mAuth;
    // [END declare_auth]

    @Inject
    GoogleSignInClient mGoogleSignInClient;

    @Inject
    ViewModelProviderFactory providerFactory;

    private GoogleSignInViewModel viewModel;

    private com.google.android.gms.common.SignInButton signInButton;

    private Button signOutButton, disconnectButton;
    private TextView status, detail;
    private LinearLayout signOutAndDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google2);

        mProgressBar = findViewById(R.id.progressBar);
        signInButton = findViewById(R.id.sign_in_button);
        signOutButton = findViewById(R.id.signOutButton);
        //   disconnectButton = findViewById(R.id.disconnectButton);
        //   status = findViewById(R.id.status);
        //   detail = findViewById(R.id.detail);

        //  signOutAndDisconnect = findViewById(R.id.signOutAndDisconnect);

        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        //   disconnectButton.setOnClickListener(this);

        viewModel = new ViewModelProvider(this, providerFactory).get(GoogleSignInViewModel.class);

        //subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<FirebaseUser>>() {
            @Override
            public void onChanged(AuthResource<FirebaseUser> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {

                        case LOADING: {
                            showProgressBar(true);
                            Log.d(TAG, "onChanged: LOGIN LOADING: ");
                            break;
                        }

                        case AUTHENTICATED: {
                            showProgressBar(false);
                            updateUI(userAuthResource.data);
                            Log.d(TAG, "onChanged: LOGIN SUCCESS: " + userAuthResource.data.getEmail());

                            break;
                        }

                        case ERROR: {
                            showProgressBar(false);
                            Toast.makeText(GoogleSignInActivity.this, userAuthResource.message
                                    + "\nDid you enter a number between 1 and 10?", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        case NOT_AUTHENTICATED: {
                            showProgressBar(false);
                            break;
                        }
                    }
                }
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
        if (requestCode == RC_SIGN_OUT) {
            signOut();
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        showProgressBar(true);
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            viewModel.authenticateWithId(0);
                            startActivity(new Intent(GoogleSignInActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //  hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "signIn: ");
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            //  status.setText(getString(R.string.google_status_fmt, user.getEmail()));
            //  detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //  signInButton.setVisibility(View.GONE);
            //   signOutAndDisconnect.setVisibility(View.VISIBLE);
        } else {
            //  status.setText(R.string.signed_out);
            //   detail.setText(null);

            //  signInButton.setVisibility(View.VISIBLE);
            //  signOutAndDisconnect.setVisibility(View.GONE);
        }
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