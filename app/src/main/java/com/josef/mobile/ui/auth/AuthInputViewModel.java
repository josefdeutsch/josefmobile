package com.josef.mobile.ui.auth;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.ui.base.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class AuthInputViewModel extends BaseViewModel {

    private static final String TAG = "AuthInputViewModel";

    public final Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
    private final MediatorLiveData<CharSequence> emailInput = new MediatorLiveData<>();
    private final MediatorLiveData<CharSequence> passwordInput = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> viewVerification = new MediatorLiveData<>();
    public Matcher matcher;
    private String charSequence = "";

    private Observable<CharSequence> emailObservable;
    private Observable<CharSequence> passwordObserveable;
    private Observable<Boolean> viewObserveable;

    @Inject
    public AuthInputViewModel() {
        emailObservable = Observable.just("default");
        passwordObserveable = Observable.just("default");
    }

    public MediatorLiveData<Boolean> observeViewsVerifcations() {
        return viewVerification;
    }

    public MediatorLiveData<CharSequence> observeEmailInput() {
        return emailInput;
    }

    public MediatorLiveData<CharSequence> observePasswordInput() {
        return passwordInput;
    }

    public void verifyEmailInput(CharSequence input) {
        if (!charSequence.equals(input)) {
            charSequence = input.toString();
            LiveData<CharSequence> source =
                    LiveDataReactiveStreams.fromPublisher(getEmailObserveable(input));
            emailInput.addSource(source, listResource -> {
                emailInput.setValue(listResource);
                emailInput.removeSource(source);
            });
        }
    }

    public void verifyPasswordInput(CharSequence input) {
        if (!charSequence.equals(input)) {
            charSequence = input.toString();
            LiveData<CharSequence> source =
                    LiveDataReactiveStreams.fromPublisher(getPasswordObserveable(input));
            passwordInput.addSource(source, listResource -> {
                passwordInput.setValue(listResource);
                passwordInput.removeSource(source);
            });
        }
    }

    public void verifyViewInput() {
        LiveData<Boolean> source =
                LiveDataReactiveStreams.fromPublisher(getViewsVerification());
        viewVerification.addSource(source, listResource -> {
            viewVerification.setValue(listResource);
            viewVerification.removeSource(source);
        });
    }

    @NotNull
    private Flowable<CharSequence> getPasswordObserveable(CharSequence chars) {
        emailObservable = Observable.just(chars)
                .doOnNext(charSequence -> hideEmailError())

                .filter(charSequence -> !TextUtils.isEmpty(charSequence));

        return emailObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    @NotNull
    private Flowable<CharSequence> getEmailObserveable(CharSequence chars) {
        passwordObserveable = Observable.just(chars)
                .doOnNext(charSequence -> hideEmailError())

                .filter(charSequence -> !TextUtils.isEmpty(charSequence));

        return passwordObserveable.toFlowable(BackpressureStrategy.BUFFER);
    }

    private Flowable<Boolean> getViewsVerification() {

        viewObserveable = Observable.combineLatest(emailObservable, passwordObserveable,
                new BiFunction<CharSequence, CharSequence, Boolean>() {

                    @NonNull
                    @Override
                    public Boolean apply(@NonNull CharSequence email, @NonNull CharSequence password) throws Exception {
                        Log.d(TAG, "apply: " + email.toString());
                        boolean isEmailValid = validateEmail(email.toString());
                        Log.d(TAG, "validateEmail: " + isEmailValid);
                        boolean isPasswordValid = validatePassword(password.toString());
                        Log.d(TAG, "validatePassword: " + isPasswordValid);
                        return isEmailValid && isPasswordValid;
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());

        return viewObserveable.toFlowable(BackpressureStrategy.BUFFER);

    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) return false;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        return password.length() > 8;
    }


    private void hideEmailError() {

    }

}
