package com.josef.mobile.vfree.ui.auth;


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


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.vfree.ui.auth.option.account.SignActivity;
import com.josef.mobile.vfree.ui.auth.option.verification.VerificationActivity;
import com.josef.mobile.vfree.ui.base.BaseActivity;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.splash.SplashActivity;
import com.josef.mobile.vfree.utils.UtilManager;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public final class AuthActivity extends BaseActivity {

    public static final int RC_SIGN_OUT = 9001;
    public static final int SU_SIGN_IN = 9003;
    public static final int VU_SIGN_IN = 9004;
    public static final int AUTH_REQ = 9005;

    @Inject
    FirebaseAuth mAuth;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.forgot_password_btn)
    Button forgotPasswordButton;
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
    @BindView(R.id.logo)
    ImageView imageView;
    @BindView(R.id.buttons_ll)
    LinearLayout actionButtons;

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

        verifyEmailInputs();
        verifyPasswordInputs();
        observeEmailInputs();
        observePasswordInputs();
        observeLayoutActivation();
        observeFirebaseValidation();

        onKeyBoardEventListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
      //  hideProgessbar();
    }

    @Override
    public void onBackPressed() {
        alert.setMessage(R.string.app_quit_remainder);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> finish());
        alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }

    private void onKeyBoardEventListener() {
        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen) {
                imageView.setVisibility(View.GONE);
                actionButtons.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                actionButtons.setVisibility(View.VISIBLE);
            }
        });
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

        if (requestCode == AUTH_REQ && Activity.RESULT_OK == resultCode) {
            signOut();
        }
        if (requestCode == RC_SIGN_OUT) {
            signOut();
        }
    }

    private void observeLayoutActivation() {
        authInputViewModel.getCombiner().removeObservers(this);
        authInputViewModel.getCombiner().observe(this, quartet -> {

            boolean isEmailValid = validateEmail(quartet.c);
            boolean isPasswordValid = validatePassword(quartet.d);

            if (isEmailValid && isPasswordValid) {
                enableSignIn();
            } else {
                disableSignIn();
            }
        });
    }

    private void observePasswordInputs() {
        authInputViewModel.getPasswordHelper().getLiveData().removeObservers(this);
        authInputViewModel.getPasswordHelper().getLiveData().observe(this, charSequence -> {
            boolean isPasswordValid = validatePassword(charSequence.toString());
            if (!isPasswordValid) {
                showPasswordError();
            } else {
                hidePasswordError();
            }
        });
    }

    private void observeEmailInputs() {
        authInputViewModel.getEmailHelper().getLiveData().removeObservers(this);
        authInputViewModel.getEmailHelper().getLiveData().observe(this, charSequence -> {
            boolean isEmailValid = validateEmail(charSequence.toString());
            if (!isEmailValid) {
                showEmailError();
            } else {
                hideEmailError();
            }
        });
    }


    private void verifyPasswordInputs() {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                authInputViewModel.verifyPasswordInputs(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                authInputViewModel.verifyUsersInput();
            }
        });
    }


    private void verifyEmailInputs() {
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                authInputViewModel.verifyEmailInputs(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                authInputViewModel.verifyUsersInput();
            }
        });
    }

    public void observeFirebaseValidation() {
        authViewModel.observeAuthenticatedUser().removeObservers(this);
        authViewModel.observeAuthenticatedUser().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case LOADING: {
                        showAuthProgressbar(AuthActivity.this);
                        break;
                    }
                    case AUTHENTICATED: {
                        Bundle bundle = ActivityOptionsCompat
                                .makeCustomAnimation(AuthActivity.this,
                                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivityForResult(new Intent(AuthActivity.this, MainActivity.class), AUTH_REQ,bundle);
                        //new Handler().postDelayed(() -> utilManager.hideProgressbar(), 1000);
                        finishAfterTransition();
                        break;
                    }
                    case ERROR: {
                        hideProgessbar();
                        Toast.makeText(AuthActivity.this, userAuthResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case NOT_AUTHENTICATED: {
                        break;
                    }
                }
            }
        });
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
        Intent signUpIntent = new Intent(this, VerificationActivity.class);
        startActivityForResult(signUpIntent, VU_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void showEmailError() {
        enableError(emailInputLayout);
        emailInputLayout.setError("Invalid email");
    }

    private void hideEmailError() {
        disableError(emailInputLayout);
        emailInputLayout.setErrorEnabled(false);
    }

    private void enableError(TextInputLayout textInputLayout) {
        if (textInputLayout.getChildCount() == 2)
            textInputLayout.getChildAt(1).setVisibility(View.VISIBLE);
    }

    private void disableError(TextInputLayout textInputLayout) {
        if (textInputLayout.getChildCount() == 2)
            textInputLayout.getChildAt(1).setVisibility(View.GONE);
    }

    private boolean validateEmail(CharSequence email) {
        return utilManager.validateEmail(email);
    }

    private boolean validatePassword(CharSequence password) {
        if (password == null) return false;
        return password.length() > 5;
    }

    private void showPasswordError() {
        enableError(passwordInputLayout);
        passwordInputLayout.setError("Invalid password");
    }

    private void hidePasswordError() {
        disableError(passwordInputLayout);
        passwordInputLayout.setErrorEnabled(false);
    }

    private void enableSignIn() {
        linearLayoutSignIn.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        signInWithEmail.setEnabled(true);
        signInWithEmail.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
    }

    private void disableSignIn() {
        linearLayoutSignIn.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        signInWithEmail.setEnabled(false);
        signInWithEmail.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }


    @Override
    public void subscribeToSessionManager() {

    }
}