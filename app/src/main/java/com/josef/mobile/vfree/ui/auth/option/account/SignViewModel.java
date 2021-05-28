package com.josef.mobile.vfree.ui.auth.option.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import static android.content.ContentValues.TAG;


public final class SignViewModel extends BaseViewModel {

    private final FirebaseAuth firebaseAuth;
    private final DataManager dataManager;
    private final Context context;

    private final MediatorLiveData<Resource<User>> containers = new MediatorLiveData<>();

    @Inject
    public SignViewModel(@NonNull FirebaseAuth firebaseAuth,
                         @NonNull DataManager dataManager,
                         @NonNull Context context) {
        this.firebaseAuth = firebaseAuth;
        this.dataManager = dataManager;
        this.context = context;
    }

    @NonNull
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

    @NonNull
    private Flowable<Resource<User>> getFlowableResourceBoolean(@NonNull String fName,
                                                                @NonNull String lName,
                                                                @NonNull String email,
                                                                @NonNull String password) {

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

    @NonNull
    private Single<User> createAccount(@NonNull String email,
                                       @NonNull String password) {

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

    @NonNull
    private Single<User> sendVerification() {
        return Single.create(emitter -> {
            firebaseAuth.getCurrentUser().reload();
            firebaseAuth.getCurrentUser()
                    .sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User user = new User();
                            emitter.onSuccess(user);
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    @NonNull
    private Single<User> createCredentials(@NonNull String fName,
                                           @NonNull String lName,
                                           @NonNull String email) {
        return Single.create(emitter -> {

            DatabaseReference myRef = dataManager.getDataBaseRefChild_Profile();

            Log.d(TAG, "createCredentials: ");

            User user = new User(email, fName, lName);

            // user.setFname(fName);
            // user.setLname(lName);
            // user.setEmail(email);

            Log.d(TAG, "createCredentials: " + firebaseAuth.getCurrentUser().getUid());
            Log.d(TAG, "createCredentials: " + fName + lName + email);

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
