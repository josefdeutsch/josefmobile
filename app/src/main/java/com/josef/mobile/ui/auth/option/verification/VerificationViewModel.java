package com.josef.mobile.ui.auth.option.verification;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.ui.auth.model.CharSequenceObserver;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class VerificationViewModel extends BaseViewModel {


    private static final String TAG = "VerificationViewModel";

    private final Context mContext;
    private final FirebaseAuth firebaseAuth;

    private final MediatorLiveData<Resource<User>> containers = new MediatorLiveData<>();
    private final MediatorLiveData<CharSequence> emailText = new MediatorLiveData<>();

    private final CharSequenceObserver<CharSequence> emailTextObserver = new CharSequenceObserver();

    private String verifier = "";

    @Inject
    public VerificationViewModel(Context context, FirebaseAuth firebaseAuth) {
        this.mContext = context;
        this.firebaseAuth = firebaseAuth;
    }

    public MediatorLiveData<CharSequence> observeEmailText() {
        return emailText;
    }

    public void verifyEmailInputs(CharSequence input) {
        if (!verifier.equals(input)) {
            verifier = input.toString();
            LiveData<CharSequence> source =
                    LiveDataReactiveStreams.fromPublisher(getCharSequenceObservable(input));
            emailText.addSource(source, listResource -> {
                emailText.setValue(listResource);
                emailText.removeSource(source);
            });
        }
    }


    @NotNull
    private Flowable<CharSequence> getCharSequenceObservable(CharSequence sequence) {
        emailTextObserver.setSubject(sequence);
        return emailTextObserver.generateObservable()
                .doOnNext(charSequence -> hideEmailError())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());
    }


    private void hideEmailError() {

    }


    public MediatorLiveData<Resource<User>> observeContainer() {
        return containers;
    }


    public void sendPasswordResetEmail(String email) {

        LiveData<Resource<User>> source =
                LiveDataReactiveStreams.fromPublisher(getFlowableResourceUser(email));

        containers.setValue(Resource.loading(null));

        containers.addSource(source, listResource -> {
            containers.setValue(listResource);
            containers.removeSource(source);
        });
    }

    private Flowable<Resource<User>> getFlowableResourceUser(String email) {
        return addonVerificationListenerSingle(email)
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.toString());
                    User user = new User();
                    user.setId(-1);
                    user.setThrowable(throwable);
                    return user;
                })
                .map((Function<User, Resource<User>>) user -> {
                    if (user.getId() == -1) {
                        return Resource.error(user.getThrowable().getMessage(), null);
                    }
                    return Resource.success(user);
                })
                .toFlowable()
                .subscribeOn(Schedulers.io());
    }

    private Single<User> addonVerificationListenerSingle(String email) {
        return Single.create(emitter -> firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User();
                        emitter.onSuccess(user);
                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }
}
