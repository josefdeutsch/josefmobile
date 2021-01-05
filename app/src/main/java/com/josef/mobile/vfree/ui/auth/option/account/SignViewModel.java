package com.josef.mobile.vfree.ui.auth.option.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class SignViewModel extends BaseViewModel {


    private final FirebaseAuth firebaseAuth;
    private final MediatorLiveData<Resource<User>> containers = new MediatorLiveData<>();

    @Inject
    public SignViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public MediatorLiveData<Resource<User>> getContainers() {
        return containers;
    }

    public void createUserWithEmailandPassword(String email, String password) {

        LiveData<Resource<User>> source =
                LiveDataReactiveStreams.fromPublisher(getFlowableResourceBoolean(email, password));

        containers.setValue(Resource.loading(null));

        containers.addSource(source, listResource -> {
            containers.setValue(listResource);
            containers.removeSource(source);
        });
    }

    private Flowable<Resource<User>> getFlowableResourceBoolean(String email, String password) {

        Single<User> completion = addOnCompletionListenerSingle(email, password);
        Single<User> verification = addonVerificationListenerSingle();

        return concatOptionsSingles(completion, verification)
                .onErrorReturn(throwable -> {
                    User user = new User();
                    user.setId(-1);
                    user.setThrowable(throwable);
                    return user;
                })
                .map(user -> {
                    if (user.getId() == -1) {
                    }
                    return Resource.success(user);
                })
                .subscribeOn(Schedulers.io());
    }


    private Flowable<User> concatOptionsSingles(Single<User> completion, Single<User> verification) {
        return completion.flatMap(completion1 -> verification).toFlowable();
    }

    private Single<User> addonVerificationListenerSingle() {
        return Single.create(emitter -> firebaseAuth.getCurrentUser()
                .sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User();
                        emitter.onSuccess(user);
                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }

    private Single<User> addOnCompletionListenerSingle(String email, String password) {
        return Single.create(emitter -> firebaseAuth
                .createUserWithEmailAndPassword(email, password)
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
