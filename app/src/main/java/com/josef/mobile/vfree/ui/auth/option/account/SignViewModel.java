package com.josef.mobile.vfree.ui.auth.option.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public final class SignViewModel extends BaseViewModel {

    private final FirebaseAuth firebaseAuth;
    private final DataManager dataManager;
    private final Context context;

    private static final String TAG = "SignViewModel";

    private final MediatorLiveData<Resource<User>> containers = new MediatorLiveData<>();

    @Inject
    public SignViewModel(FirebaseAuth firebaseAuth, DataManager dataManager, Context context) {
        this.firebaseAuth = firebaseAuth;
        this.dataManager = dataManager;
        this.context = context;
    }

    public MediatorLiveData<Resource<User>> getContainers() {
        return containers;
    }

    public void createUserWithEmailandPassword(
            String fName,
            String lName,
            String email,
            String password) {

        LiveData<Resource<User>> source =
                LiveDataReactiveStreams.fromPublisher(getFlowableResourceBoolean(fName, lName, email, password));

        containers.setValue(Resource.loading(null));

        containers.addSource(source, listResource -> {
            containers.setValue(listResource);
            containers.removeSource(source);
        });
    }

    private Flowable<Resource<User>> getFlowableResourceBoolean(String fName,
                                                                String lName,
                                                                String email,
                                                                String password) {

         Single<User> createAccount = createAccount(email, password);

         Single<User> sendVerification = sendVerification();

         Single<User> createCredentials = createCredentials(fName, lName, email);

        return createAccount
                .flatMap(user -> sendVerification)
                .flatMap(user -> createCredentials)

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
                .toFlowable()
                .subscribeOn(Schedulers.io());
    }

    private Single<User> createAccount(String email, String password) {
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

    private Single<User> sendVerification() {
        return Single.create(new SingleOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<User> emitter) throws Exception {
                firebaseAuth.getCurrentUser().reload();
                firebaseAuth.getCurrentUser()
                        .sendEmailVerification()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User user = new User();
                                emitter.onSuccess(user);
                                Log.d(TAG, "sendVerification: ");
                            } else {
                                emitter.onError(task.getException());
                            }
                        });
            }
        });
    }

    private Single<User> createCredentials(String fName, String lName, String email) {
        return Single.create(emitter -> {

            DatabaseReference myRef = dataManager.getDataBaseRefChild_Profile();

            User user = new User();
            user.setFname(fName);
            user.setLname(lName);
            user.setEmail(email);

            myRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(user)
                    .addOnCompleteListener(task -> {
                        emitter.onSuccess(null);
                    })
                    .addOnFailureListener(exception -> {
                        emitter.onError(exception);
                    });
        });
    }

}
