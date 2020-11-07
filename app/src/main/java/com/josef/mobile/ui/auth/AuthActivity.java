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
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.R;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.auth.sign.SignActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class AuthActivity extends BaseActivity implements View.OnClickListener {

    public static final int RC_SIGN_OUT = 9001;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int SU_SIGN_IN = 9002;

    @Inject
    FirebaseAuth mAuth;
    @Inject
    GoogleSignInClient mGoogleSignInClient;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;

    private AuthViewModel viewModel;

    private EditText emailEditText, passwordEditText;
    private com.google.android.gms.common.SignInButton signInButton;
    private Button signUpWithEmail;
    private Button signInWithEmail;

    Task<GoogleSignInAccount> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout2);
        emailEditText = findViewById(R.id.email_et);
        passwordEditText = findViewById(R.id.password_et);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signUpWithEmail = findViewById(R.id.sign_up_btn);
        signUpWithEmail.setOnClickListener(this);

        signInWithEmail = findViewById(R.id.sign_in_with_email);
        signInWithEmail.setOnClickListener(this);

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
            task = GoogleSignIn.getSignedInAccountFromIntent(data);
            viewModel.authenticateWithGoogle(task);
        }
        if (requestCode == SU_SIGN_IN) {
            //viewModel.authenticateWithEmail();
        }
        if (requestCode == RC_SIGN_OUT) {
            signOut();
        }
    }

    public void subscribeObserver() {

        viewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);
        viewModel.observeAuthenticatedUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case LOADING: {
                            utilManager.showProgressbar(AuthActivity.this);
                            break;
                        }

                        case AUTHENTICATED: {
                            Log.d(TAG, "onChanged: AuthActivity: AUTHENTICATED... " +
                                    "Authenticated as: " + userAuthResource.data.getEmail());
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                            utilManager.hideProgressbar();
                            break;
                        }

                        case ERROR: {
                            Log.d(TAG, "onChanged: AuthActivity: ERROR...");
                            utilManager.hideProgressbar();
                            Toast.makeText(AuthActivity.this, userAuthResource.message, Toast.LENGTH_SHORT).show();
                            break;
                        }

                        case NOT_AUTHENTICATED: {
                            Log.d(TAG, "onChanged: AuthActivity: NOT AUTHENTICATED. Navigating to Login screen.");
                            break;
                        }
                    }
                }
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithEmail() {
        viewModel.authenticateWithEmail(
                emailEditText.getText().toString(), passwordEditText.getText().toString());
    }


    private void signUpWithEmail() {
        Intent signUpIntent = new Intent(this, SignActivity.class);
        startActivityForResult(signUpIntent, SU_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        if (mGoogleSignInClient != null) mGoogleSignInClient.signOut();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.sign_in_button) {
            signInWithGoogle();
        }

        if (i == R.id.sign_up_btn) {
            signUpWithEmail();
        }

        if (i == R.id.sign_in_with_email) {
            signInWithEmail();
        }

    }

    @Override
    public void subscribeToSessionManager() {

    }
}