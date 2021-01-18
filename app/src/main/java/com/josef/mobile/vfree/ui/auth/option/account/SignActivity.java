package com.josef.mobile.vfree.ui.auth.option.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.josef.mobile.vfree.ui.auth.AuthInputViewModel;
import com.josef.mobile.vfree.ui.auth.email.help.Quartet;
import com.josef.mobile.vfree.ui.base.BaseActivity;
import com.josef.mobile.vfree.utils.UtilManager;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class SignActivity extends BaseActivity {

    AuthInputViewModel authInputViewModel;

    SignViewModel viewModel;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;

    @BindView(R.id.firstName_et)
    EditText firstNameEditText;
    @BindView(R.id.firstName_til)
    TextInputLayout firstNameInputLayout;

    @BindView(R.id.lastName_et)
    EditText lastNameEditText;
    @BindView(R.id.lastName_til)
    TextInputLayout lastNameInputLayout;

    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.email_til)
    TextInputLayout emailInputLayout;

    @BindView(R.id.password_et)
    EditText passwordEditText;
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

        verifyFirstNameInputs();
        verifyLastNameInputs();
        verifyEmailInputs();
        verifyPasswordInputs();

        observeFirstNameInputs();
        observeLastNameInputs();
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

    private static final String TAG = "SignActivity";

    private void observeLayoutActivation() {
        authInputViewModel.getCombiner().removeObservers(this);
        authInputViewModel.getCombiner().observe(this, new Observer<Quartet<CharSequence, CharSequence, CharSequence, CharSequence>>() {
            @Override
            public void onChanged(Quartet<CharSequence, CharSequence, CharSequence, CharSequence> quartet) {

                boolean isFirstNameValid = SignActivity.this.validateFirstLastName(quartet.a);
                boolean isLastNameValid = SignActivity.this.validateFirstLastName(quartet.b);
                boolean isEmailValid = SignActivity.this.validateEmail(quartet.c);
                boolean isPasswordValid = SignActivity.this.validatePassword(quartet.d);

                Log.d(TAG, "onChanged: "
                        + "firstName :" + isFirstNameValid
                        + "lastName :" + isLastNameValid
                        + "isEmailValid :" + isEmailValid
                        + "isPasswordValid :" + isPasswordValid
                );


                if (isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid) {
                    SignActivity.this.enableSignIn();
                } else {
                    SignActivity.this.disableSignIn();
                }
            }
        });
    }

    private void observeFirstNameInputs() {
        authInputViewModel.getFirstNameHelper().getLiveData().removeObservers(this);
        authInputViewModel.getFirstNameHelper().getLiveData().observe(this, charSequence -> {

            boolean isFirstNameValid = validateFirstLastName(charSequence.toString());

            if (!isFirstNameValid) {
                showFirstNameNameError();
            } else {
                hideFirstNameError();
            }
        });
    }

    private void observeLastNameInputs() {
        authInputViewModel.getLastNameHelper().getLiveData().removeObservers(this);
        authInputViewModel.getLastNameHelper().getLiveData().observe(this, charSequence -> {

            boolean isLastNameValid = validateFirstLastName(charSequence.toString());

            if (!isLastNameValid) {
                showLastNameNameError();
            } else {
                hideLastNameError();
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

    private void verifyFirstNameInputs() {
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: "+s);
                authInputViewModel.verifyFirstInputs(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                authInputViewModel.verifyUsersInput();
            }
        });
    }

    private void verifyLastNameInputs() {
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: "+s);
                authInputViewModel.verifyLastInputs(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                authInputViewModel.verifyUsersInput();
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
                Log.d(TAG, "onTextChanged: "+s);
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
                Log.d(TAG, "onTextChanged: "+s);
                authInputViewModel.verifyEmailInputs(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                authInputViewModel.verifyUsersInput();
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

    private boolean validateFirstLastName(CharSequence password) {
        if (password == null) return false;
        return password.length() > 1;
    }

    private void showPasswordError() {
        enableError(passwordInputLayout);
        passwordInputLayout.setError(this.getResources().getString(R.string.activity_auth_invalid_password));
    }

    private void showFirstNameNameError() {
        enableError(firstNameInputLayout);
        firstNameInputLayout.setError(this.getResources().getString(R.string.activity_auth_invalid_firstName));
    }

    private void showLastNameNameError() {
        enableError(lastNameInputLayout);
        lastNameInputLayout.setError(this.getResources().getString(R.string.activity_auth_invalid_lastName));
    }

    private void hidePasswordError() {
        disableError(passwordInputLayout);
        passwordInputLayout.setErrorEnabled(false);
    }

    private void hideFirstNameError() {
        disableError(firstNameInputLayout);
        firstNameInputLayout.setErrorEnabled(false);
    }

    private void hideLastNameError() {
        disableError(lastNameInputLayout);
        lastNameInputLayout.setErrorEnabled(false);
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