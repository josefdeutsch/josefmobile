package com.josef.josefmobile.ui.auth.option.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.josef.josefmobile.ui.auth.AuthInputViewModel;
import com.josef.josefmobile.ui.base.BaseActivity;
import com.josef.josefmobile.utils.UtilManager;
import com.josef.josefmobile.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {

    AuthInputViewModel authInputViewModel;

    SignViewModel viewModel;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.password_et)
    EditText passwordEditText;
    @BindView(R.id.email_til)
    TextInputLayout emailInputLayout;
    @BindView(R.id.password_til)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.sign_in_btn)
    Button signInButton;
    @BindView(R.id.sign_in_ll)
    LinearLayout linearLayoutSignIn;
    @BindView(R.id.logo)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this, providerFactory).get(SignViewModel.class);
        authInputViewModel = new ViewModelProvider(this, providerFactory).get(AuthInputViewModel.class);
        verifyEmailInputs();
        verifyPasswordInputs();
        observeEmailInputs();
        observePasswordInputs();
        observeLayoutActivation();
        observeFirebaseValidation();

        onKeyBoardEventListener();
    }

    private void onKeyBoardEventListener() {
        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void observeLayoutActivation() {
        authInputViewModel.getCombiner().removeObservers(this);
        authInputViewModel.getCombiner().observe(this, charSequenceCharSequencePair -> {

            boolean isEmailValid = validateEmail(charSequenceCharSequencePair.first);
            boolean isPasswordValid = validatePassword(charSequenceCharSequencePair.second);

            if (isEmailValid && isPasswordValid) {
                enableSignIn();
            } else {
                disableSignIn();
            }
        });
    }

    private void observePasswordInputs() {
        authInputViewModel.getPasswordText().removeObservers(this);
        authInputViewModel.getPasswordText().observe(this, charSequence -> {
            boolean isPasswordValid = validatePassword(charSequence.toString());
            if (!isPasswordValid) {
                showPasswordError();
            } else {
                hidePasswordError();
            }
        });
    }

    private void observeEmailInputs() {
        authInputViewModel.getEmailText().removeObservers(this);
        authInputViewModel.getEmailText().observe(this, charSequence -> {
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
                authInputViewModel.verifyUsersInputs();
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
                authInputViewModel.verifyUsersInputs();
            }
        });
    }

    private void observeFirebaseValidation() {
        viewModel.getContainers().removeObservers(this);
        viewModel.getContainers().observe(this, userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case LOADING: {
                        showProgressbar(SignActivity.this);
                        break;
                    }
                    case SUCCESS: {
                        hideProgessbar();
                        Toast.makeText(SignActivity.this, this.getResources().getString(R.string.activity_sign_confirmation)
                                , Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                    case ERROR: {
                        hideProgessbar();
                        Toast.makeText(SignActivity.this, userResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

    }


    @OnClick(R.id.sign_in_btn)
    void onClick(View v) {
        viewModel.createUserWithEmailandPassword(
                emailEditText.getText().toString(), passwordEditText.getText().toString());
    }


    private void showEmailError() {
        enableError(emailInputLayout);
        emailInputLayout.setError(this.getResources().getString(R.string.activity_auth_invalid_email));
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
        passwordInputLayout.setError(this.getResources().getString(R.string.activity_auth_invalid_password));
    }

    private void hidePasswordError() {
        disableError(passwordInputLayout);
        passwordInputLayout.setErrorEnabled(false);
    }

    private void enableSignIn() {
        linearLayoutSignIn.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        signInButton.setEnabled(true);
        signInButton.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
    }

    private void disableSignIn() {
        linearLayoutSignIn.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        signInButton.setEnabled(false);
        signInButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }


    @Override
    public void subscribeToSessionManager() {

    }


}