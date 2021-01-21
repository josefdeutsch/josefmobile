package com.josef.mobile.vfree.ui.auth.option.account;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.ui.main.Resource;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
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

        final Single<User> createFirebaseAccounts = createFirebaseAccount(email, password);

        final Single<User> sendVerificationToFirebases = sendVerificationToFirebase();

        final Single<User> createFirestoreCredentialss = createFirestoreCredentials(fName, lName, email);

        return createFirebaseAccounts
                .flatMap(user -> sendVerificationToFirebases)
                .flatMap(user -> createFirestoreCredentialss)

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
                }).toFlowable()
                .subscribeOn(Schedulers.io());
    }

    private Single<User> createFirebaseAccount(String email, String password) {
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

    private Single<User> sendVerificationToFirebase() {
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

    private Single<User> createFirestoreCredentials(String fName, String lName, String email) {
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
