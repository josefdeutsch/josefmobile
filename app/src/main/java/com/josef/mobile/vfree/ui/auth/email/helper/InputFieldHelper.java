package com.josef.mobile.vfree.ui.auth.email.helper;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public final class InputFieldHelper {

    @NotNull
    private final MediatorLiveData<CharSequence> mediatorLiveData = new MediatorLiveData<>();

    @NotNull
    private final CharSequenceObserver<CharSequence> observer = new CharSequenceObserver();

    @NotNull
    private LiveData<CharSequence> liveData;

    @NotNull
    private String string = "";

    public InputFieldHelper(){
        buildLiveData(string);
    }

    @NotNull
    public LiveData<CharSequence> getLiveData() {
        return liveData;
    }

    @NotNull
    public LiveData<CharSequence> buildLiveData(@NotNull CharSequence sequence) {
        return liveData = LiveDataReactiveStreams.fromPublisher(getCharSequenceObserveable(sequence)
                .toFlowable(BackpressureStrategy.BUFFER));
    }

    @NotNull
    private Observable<CharSequence> getCharSequenceObserveable(@NotNull CharSequence sequence) {
        observer.setSubject(sequence);
        return observer.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .subscribeOn(Schedulers.io());
    }

    public void verifyInputs(@NotNull CharSequence input) {

        if (!string.equals(input)) {
            string = input.toString();
            LiveData<CharSequence> liveData = buildLiveData(input);
            mediatorLiveData.addSource(liveData, listResource -> {
                mediatorLiveData.setValue(listResource);
                mediatorLiveData.removeSource(liveData);
            });
        }
    }

    private void hideEmailError() {

    }

}
