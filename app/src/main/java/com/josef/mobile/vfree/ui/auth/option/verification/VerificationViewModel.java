package com.josef.mobile.vfree.ui.auth.option.verification;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;
import javax.inject.Inject;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public final class VerificationViewModel extends BaseViewModel {

    private final FirebaseAuth firebaseAuth;

    private final MediatorLiveData<Resource<User>> containers = new MediatorLiveData<>();

    @Inject
    public VerificationViewModel(@NonNull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }
    @NonNull
    public MediatorLiveData<Resource<User>> observeContainer() {
        return containers;
    }


    public void sendPasswordResetEmail(@NonNull String email) {

        LiveData<Resource<User>> source =
                LiveDataReactiveStreams.fromPublisher(getFlowableResourceUser(email));

        containers.setValue(Resource.loading(null));

        containers.addSource(source, listResource -> {
            containers.setValue(listResource);
            containers.removeSource(source);
        });
    }

    @NonNull
    private Flowable<Resource<User>> getFlowableResourceUser(@NonNull String email) {
        return addonVerificationListenerSingle(email)
                .onErrorReturn(throwable -> {
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

    @NonNull
    private Single<User> addonVerificationListenerSingle(@NonNull String email) {
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
