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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.R;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.auth.option.account.SignActivity;
import com.josef.mobile.ui.auth.option.verification.VerificationActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class AuthActivity extends BaseActivity {


    public static final int RC_SIGN_IN = 9002;

    public static final int RC_SIGN_OUT = 9001;
    public static final int SU_SIGN_IN = 9003;
    public static final int VU_SIGN_IN = 9004;
    private static final String TAG = "GoogleActivity";

    @Inject
    FirebaseAuth mAuth;
    @Inject
    GoogleSignInClient mGoogleSignInClient;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.forgot_password_btn)
    Button forgotPasswordButton;
    @BindView(R.id.google_sign_in)
    SignInButton googlesSignIn;
    @BindView(R.id.email_sign_up_btn)
    Button signInWithGoogle;
    @BindView(R.id.password_et)
    EditText passwordEditText;
    @BindView(R.id.email_til)
    TextInputLayout emailInputLayout;
    @BindView(R.id.password_til)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.sign_in_with_email)
    Button signInWithEmail;
    @BindView(R.id.sign_in_ll)
    LinearLayout linearLayoutSignIn;

    AuthViewModel authViewModel;
    AuthInputViewModel authInputViewModel;
    Task<GoogleSignInAccount> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout2);
        ButterKnife.bind(this);
        authViewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);
        authInputViewModel = new ViewModelProvider(this, providerFactory).get(AuthInputViewModel.class);
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
            authViewModel.authenticateWithGoogle(task);
        }
        if (requestCode == SU_SIGN_IN) {
            //viewModel.authenticateWithEmail();
        }
        if (requestCode == RC_SIGN_OUT) {
            signOut();
        }
    }

    public void subscribeObserver() {

        authViewModel.observeAuthenticatedUser().observe(this, new Observer<AuthResource<User>>() {
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

    @OnClick(R.id.google_sign_in)
    void signInWithGoogle(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.sign_in_with_email)
    void signInWithEmail(View v) {
        authViewModel.authenticateWithEmail(
                emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    @OnClick(R.id.email_sign_up_btn)
    void signUpWithEmail() {
        Intent signUpIntent = new Intent(this, SignActivity.class);
        startActivityForResult(signUpIntent, SU_SIGN_IN);
    }

    @OnClick(R.id.forgot_password_btn)
    void forgotPassword() {
        Log.d(TAG, "forgotPassword: ");
        Intent signUpIntent = new Intent(this, VerificationActivity.class);
        startActivityForResult(signUpIntent, VU_SIGN_IN);
    }


    private void signOut() {
        mAuth.signOut();
        if (mGoogleSignInClient != null) mGoogleSignInClient.signOut();
    }



    @Override
    public void subscribeToSessionManager() {

    }
}