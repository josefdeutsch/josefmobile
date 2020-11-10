package com.josef.mobile.ui.auth.option.verification;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.josef.mobile.R;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignActivity";
    private final Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;
    @BindView(R.id.email_et)
    EditText emailEditText;
    @BindView(R.id.email_til)
    TextInputLayout emailInputLayout;
    @BindView(R.id.sign_in_btn)
    Button signInButton;
    @BindView(R.id.sign_in_ll)
    LinearLayout linearLayoutSignIn;
    private VerificationViewModel viewModel;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this, providerFactory).get(VerificationViewModel.class);
        signInButton.setOnClickListener(this);


        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.verifyEmailInputs(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        viewModel.observeContainer().observe(this, userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case LOADING: {
                        Log.d(TAG, "onCreate: loading");
                        utilManager.showProgressbar(VerificationActivity.this);
                        break;
                    }
                    case SUCCESS: {
                        Log.d(TAG, "onCreate: success");
                        utilManager.hideProgressbar();
                        Toast.makeText(VerificationActivity.this, "We have sent an email with a confirmation link to your email address."
                                , Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                    case ERROR: {
                        Log.d(TAG, "onCreate: error");
                        utilManager.hideProgressbar();
                        Toast.makeText(VerificationActivity.this, userResource.message, Toast.LENGTH_SHORT).show();
                        emailEditText.getText().clear();
                        break;
                    }
                }
            }
        });

        viewModel.observeEmailText().observe(this, charSequence -> {
            boolean isEmailValid = validateEmail(charSequence.toString());
            if (!isEmailValid) {
                showEmailError();
                disableSignIn();
            } else {
                hideEmailError();
                enableSignIn();
            }
        });
    }

    @Override
    public void onClick(View v) {
        viewModel.sendPasswordResetEmail(emailEditText.getText().toString());
    }


    private void showEmailError() {
        enableError(emailInputLayout);
        // emailInputLayout.setErrorEnabled(true);
        emailInputLayout.setError("invalid email..");
    }

    private void hideEmailError() {
        disableError(emailInputLayout);
        emailInputLayout.setErrorEnabled(false);
        // emailInputLayout.setError(null);
    }

    private void enableError(TextInputLayout textInputLayout) {
        if (textInputLayout.getChildCount() == 2)
            textInputLayout.getChildAt(1).setVisibility(View.VISIBLE);
    }

    private void disableError(TextInputLayout textInputLayout) {
        if (textInputLayout.getChildCount() == 2)
            textInputLayout.getChildAt(1).setVisibility(View.GONE);
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void enableSignIn() {
        linearLayoutSignIn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        signInButton.setEnabled(true);
        signInButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    }

    private void disableSignIn() {
        linearLayoutSignIn.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_500));
        signInButton.setEnabled(false);
        signInButton.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
    }


    @Override
    public void subscribeToSessionManager() {

    }


}