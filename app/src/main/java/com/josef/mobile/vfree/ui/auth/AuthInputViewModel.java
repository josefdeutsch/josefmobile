package com.josef.mobile.vfree.ui.auth;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.vfree.ui.auth.email.help.CharSequenceObserver;
import com.josef.mobile.vfree.ui.auth.email.help.CombinedLiveData;
import com.josef.mobile.vfree.ui.base.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthInputViewModel extends BaseViewModel {

    private final MediatorLiveData<CharSequence> emailText = new MediatorLiveData<>();
    private final MediatorLiveData<CharSequence> passwordText = new MediatorLiveData<>();


    private final CharSequenceObserver<CharSequence> emailTextObserver = new CharSequenceObserver();
    private final CharSequenceObserver<CharSequence> passwordTextObserver = new CharSequenceObserver();

    private LiveData<CharSequence> emailTextLiveData;
    private LiveData<CharSequence> passWordLiveData;

    private String verifier = "";

    private final CombinedLiveData<CharSequence, CharSequence> combiner = new CombinedLiveData<>();

    @Inject
    public AuthInputViewModel() {
        getPasswordLiveData("");
        getEmailLiveData("");
        combiner.combinedLiveData(emailTextLiveData, passWordLiveData);

    }

    public MediatorLiveData<CharSequence> getEmailText() {
        return emailText;
    }

    public MediatorLiveData<CharSequence> getPasswordText() {
        return passwordText;
    }

    public CombinedLiveData<CharSequence, CharSequence> getCombiner() {
        return combiner;
    }

    public void verifyEmailInputs(CharSequence input) {

        if (!verifier.equals(input)) {
            verifier = input.toString();
            LiveData<CharSequence> emailTextLiveData = getEmailLiveData(input);
            emailText.addSource(emailTextLiveData, listResource -> {
                emailText.setValue(listResource);
                emailText.removeSource(emailTextLiveData);
            });
        }
    }

    private LiveData<CharSequence> getEmailLiveData(CharSequence sequence) {
        return emailTextLiveData = LiveDataReactiveStreams.fromPublisher(getEmailCharSequenceObservable(sequence)
                .toFlowable(BackpressureStrategy.BUFFER));
    }

    @NotNull
    private Observable<CharSequence> getEmailCharSequenceObservable(CharSequence sequence) {
        emailTextObserver.setSubject(sequence);
        return emailTextObserver.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io());
    }

    public void verifyPasswordInputs(CharSequence input) {
        if (!verifier.equals(input)) {
            verifier = input.toString();
            LiveData<CharSequence> passwordLiveData = getPasswordLiveData(input);
            passwordText.addSource(passwordLiveData, listResource -> {
                passwordText.setValue(listResource);
                passwordText.removeSource(passwordLiveData);
            });
        }
    }

    private LiveData<CharSequence> getPasswordLiveData(CharSequence sequence) {
        return passWordLiveData = LiveDataReactiveStreams.fromPublisher(getPasswordCharSequenceObservable(sequence)
                .toFlowable(BackpressureStrategy.BUFFER));
    }

    @NotNull
    private Observable<CharSequence> getPasswordCharSequenceObservable(CharSequence sequence) {
        passwordTextObserver.setSubject(sequence);
        return passwordTextObserver.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io());
    }

    public void verifyUsersInputs() {
        combiner.removeLiveData(emailTextLiveData, passWordLiveData);
        combiner.combinedLiveData(emailTextLiveData, passWordLiveData);
    }

    private void hideEmailError() {

    }

}
