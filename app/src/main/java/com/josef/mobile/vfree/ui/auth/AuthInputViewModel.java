package com.josef.mobile.vfree.ui.auth;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.josef.mobile.vfree.ui.auth.email.help.CharSequenceObserver;
import com.josef.mobile.vfree.ui.auth.email.help.InputFieldHelper;
import com.josef.mobile.vfree.ui.auth.email.help.TupleLiveData;
import com.josef.mobile.vfree.ui.auth.email.help.QuartetLiveData;
import com.josef.mobile.vfree.ui.base.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthInputViewModel extends BaseViewModel {

    public QuartetLiveData<CharSequence, CharSequence, CharSequence, CharSequence> getCombiner() {
        return combiner;
    }

    private QuartetLiveData<CharSequence,CharSequence,CharSequence,CharSequence> combiner = new QuartetLiveData<>();

    private String verifier = "";

    private void hideEmailError() {

    }

    private final InputFieldHelper firstNameHelper = new InputFieldHelper();
    private final InputFieldHelper lastNameHelper = new InputFieldHelper();
    private final InputFieldHelper emailHelper = new InputFieldHelper();
    private final InputFieldHelper passwordHelper = new InputFieldHelper();


    @Inject
    public AuthInputViewModel() {
        getFirstTextLiveData("");
        getLastLiveData("");
        getEmailLiveData("");
        getPasswordLiveData("");

        combiner.combinedLiveData(firstTextLiveData,lastTextLiveData,emailTextLiveData,passWordLiveData);
    }

    public void verifyUsersInput() {

        combiner.removeLiveData(firstTextLiveData,lastTextLiveData,emailTextLiveData,passWordLiveData);
        combiner.combinedLiveData(firstTextLiveData,lastTextLiveData,emailTextLiveData,passWordLiveData);
    }

    // email
    //<------------------------------------------------------------------------------------------------------------------------->

    private final MediatorLiveData<CharSequence> emailText = new MediatorLiveData<>();

    // observes email error
    public MediatorLiveData<CharSequence> getEmailText() {
        return emailText;
    }

    private final CharSequenceObserver<CharSequence> emailTextObserver = new CharSequenceObserver();

    private LiveData<CharSequence> emailTextLiveData;
    // produces livedata to be observerd
    private LiveData<CharSequence> getEmailLiveData(CharSequence sequence) {
        return emailTextLiveData = LiveDataReactiveStreams.fromPublisher(getEmailCharSequenceObservable(sequence)
                .toFlowable(BackpressureStrategy.BUFFER));
    }

    // build and genereate obsereables
    @NotNull
    private Observable<CharSequence> getEmailCharSequenceObservable(CharSequence sequence) {
        emailTextObserver.setSubject(sequence);
        return emailTextObserver.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io());
    }

    // set in MainActivity active MVVM
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

    // password
    //<------------------------------------------------------------------------------------------------------------------------->

    private final MediatorLiveData<CharSequence> passwordText = new MediatorLiveData<>();

    public MediatorLiveData<CharSequence> getPasswordText() {
        return passwordText;
    }

    private final CharSequenceObserver<CharSequence> passwordTextObserver = new CharSequenceObserver();

    private LiveData<CharSequence> passWordLiveData;

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

    // set email and password combiner to bo observed
    private final TupleLiveData<CharSequence, CharSequence> firstnameLastname = new TupleLiveData<>();

    public TupleLiveData<CharSequence, CharSequence> getFirstname() {
        return firstnameLastname;
    }

    // email
    //<------------------------------------------------------------------------------------------------------------------------->

    private final MediatorLiveData<CharSequence> firstText = new MediatorLiveData<>();

    // observes email error
    public MediatorLiveData<CharSequence> getFirstTextText() {
        return firstText;
    }

    private final CharSequenceObserver<CharSequence> firstTextObserver = new CharSequenceObserver();

    private LiveData<CharSequence> firstTextLiveData;
    // produces livedata to be observerd
    private LiveData<CharSequence> getFirstTextLiveData(CharSequence sequence) {
        return firstTextLiveData = LiveDataReactiveStreams.fromPublisher(getFirstCharSequenceObservable(sequence)
                .toFlowable(BackpressureStrategy.BUFFER));
    }
    // build and genereate obsereables
    @NotNull
    private Observable<CharSequence> getFirstCharSequenceObservable(CharSequence sequence) {
        firstTextObserver.setSubject(sequence);
        return firstTextObserver.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io());
    }

    // set in MainActivity active MVVM
    public void verifyFirstInputs(CharSequence input) {

        if (!verifier.equals(input)) {
            verifier = input.toString();
            LiveData<CharSequence> firstLiveData = getFirstTextLiveData(input);
            firstText.addSource(firstLiveData, listResource -> {
                firstText.setValue(listResource);
                firstText.removeSource(firstLiveData);
            });
        }
    }

    // password
    //<------------------------------------------------------------------------------------------------------------------------->

    private final MediatorLiveData<CharSequence> lastText = new MediatorLiveData<>();

    public MediatorLiveData<CharSequence> getLastText() {
        return lastText;
    }

    private final CharSequenceObserver<CharSequence> lastTextObserver = new CharSequenceObserver();

    private LiveData<CharSequence> lastTextLiveData;

    private LiveData<CharSequence> getLastLiveData(CharSequence sequence) {
        return lastTextLiveData = LiveDataReactiveStreams.fromPublisher(getLastCharSequenceObservable(sequence)
                .toFlowable(BackpressureStrategy.BUFFER));
    }

    @NotNull
    private Observable<CharSequence> getLastCharSequenceObservable(CharSequence sequence) {
        lastTextObserver.setSubject(sequence);
        return lastTextObserver.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io());
    }

    public void verifyLastInputs(CharSequence input) {

        if (!verifier.equals(input)) {
            verifier = input.toString();
            LiveData<CharSequence> lastLiveData = getLastLiveData(input);
            lastText.addSource(lastLiveData, listResource -> {
                lastText.setValue(listResource);
                lastText.removeSource(lastLiveData);
            });
        }
    }


}
