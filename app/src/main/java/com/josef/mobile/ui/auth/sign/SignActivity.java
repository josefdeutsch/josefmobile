package com.josef.mobile.ui.auth.sign;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.josef.mobile.R;
import com.josef.mobile.ui.auth.AuthInputViewModel;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignActivity";
    private final Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    UtilManager utilManager;
    private SignViewModel viewModel;
    private AuthInputViewModel authViewModel;

    private EditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private Button signInButton;
    private LinearLayout linearLayoutSignIn;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        viewModel = new ViewModelProvider(this, providerFactory).get(SignViewModel.class);
        authViewModel = new ViewModelProvider(this, providerFactory).get(AuthInputViewModel.class);

        emailEditText = findViewById(R.id.email_et);
        emailInputLayout = findViewById(R.id.email_til);
        passwordEditText = findViewById(R.id.password_et);
        passwordInputLayout = findViewById(R.id.password_til);
        signInButton = findViewById(R.id.sign_in_btn);
        linearLayoutSignIn = findViewById(R.id.sign_in_ll);

        signInButton.setOnClickListener(this);

        //Observable<CharSequence> charSequenceObservableEmail = getCharSequenceObservable(emailEditText);
        //Observable<CharSequence> charSequenceObservablepassWord = getCharSequenceObservable(passwordEditText);

        //interruptInvalidEmailInput(charSequenceObservableEmail);
        //interruptInvalidPasswordInputs(charSequenceObservablepassWord);

        //verifyInputResults(charSequenceObservableEmail, charSequenceObservablepassWord);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    authViewModel.verifyEmailInput(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                authViewModel.verifyViewInput();
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    authViewModel.verifyPasswordInput(s);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                authViewModel.verifyViewInput();
            }
        });

        authViewModel.observeEmailInput().observe(this, charSequence -> {
            boolean isEmailValid = validateEmail(charSequence.toString());
            if (!isEmailValid) {
                showEmailError();
            } else {
                hideEmailError();
            }
        });

        authViewModel.observePasswordInput().observe(this, charSequence -> {
            boolean isPasswordValid = validatePassword(charSequence.toString());
            if (!isPasswordValid) {
                showPasswordError();
            } else {
                hidePasswordError();
            }
        });

        authViewModel.observeViewsVerifcations().observe(this, booleans -> {
            if (booleans) {
                Log.d(TAG, "onChanged: " + booleans);
                enableSignIn();
            } else {
                Log.d(TAG, "onChanged: " + booleans);
                disableSignIn();
            }
        });

        subscribeObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    private void subscribeObservers() {
        viewModel.observeContainers().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                if (userResource != null) {
                    switch (userResource.status) {
                        case LOADING: {
                            utilManager.showProgressbar(SignActivity.this);
                            break;
                        }
                        case SUCCESS: {
                            utilManager.hideProgressbar();
                            Toast.makeText(SignActivity.this, "We have sent an email with a confirmation link to your email address."
                                    , Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        }
                        case ERROR: {
                            utilManager.hideProgressbar();
                            Toast.makeText(SignActivity.this, userResource.message, Toast.LENGTH_SHORT).show();
                            emailEditText.getText().clear();
                            passwordEditText.getText().clear();
                            break;
                        }
                    }
                }
            }
        });

    }

    private void verifyInputResults(Observable<CharSequence> charSequenceObservableEmail, Observable<CharSequence> charSequenceObservablepassWord) {
        Observable.combineLatest(charSequenceObservableEmail, charSequenceObservablepassWord,
                (email, password) -> {
                    boolean isEmailValid = SignActivity.this.validateEmail(email.toString());
                    boolean isPasswordValid = SignActivity.this.validatePassword(password.toString());
                    return isEmailValid && isPasswordValid;
                })
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onNext(Boolean validFields) {
                        if (validFields) {
                            enableSignIn();
                        } else {
                            disableSignIn();
                        }
                    }
                });
    }


    @NotNull
    private Observable<CharSequence> getCharSequenceObservable(EditText emailEditText) {
        return RxTextView.textChanges(emailEditText)
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void interruptInvalidEmailInput(Observable<CharSequence> charSequenceObservableEmail) {
        charSequenceObservableEmail
                .subscribe(new DisposableObserver<CharSequence>() {
                    @Override
                    public void onNext(@NonNull CharSequence charSequence) {
                        boolean isEmailValid = validateEmail(charSequence.toString());
                        if (!isEmailValid) {
                            showEmailError();
                        } else {
                            hideEmailError();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void interruptInvalidPasswordInputs(Observable<CharSequence> charSequenceObservablepassWord) {
        charSequenceObservablepassWord
                .subscribe(new DisposableObserver<CharSequence>() {
                    @Override
                    public void onNext(@NonNull CharSequence charSequence) {
                        boolean isPasswordValid = validatePassword(charSequence.toString());
                        if (!isPasswordValid) {
                            showPasswordError();
                        } else {
                            hidePasswordError();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        viewModel.createUserWithEmailandPassword(
                emailEditText.getText().toString(), passwordEditText.getText().toString());
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

    private boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private void showPasswordError() {
        enableError(passwordInputLayout);
        // passwordInputLayout.setErrorEnabled(true);
        passwordInputLayout.setError("invalid password..");
    }

    private void hidePasswordError() {
        disableError(passwordInputLayout);
        passwordInputLayout.setErrorEnabled(false);
        //passwordInputLayout.setError(null);
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