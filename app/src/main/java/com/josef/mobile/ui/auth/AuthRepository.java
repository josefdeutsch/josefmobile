package com.josef.mobile.ui.auth;


import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josef.mobile.ui.intro.AuthResource;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;

@SuppressWarnings("ConstantConditions")
@Singleton
public
class AuthRepository {

    private static final String TAG = "AuthRepository";
    private FirebaseAuth auth;

    public AuthRepository(FirebaseAuth auth) {
        this.auth = auth;
    }

    public MutableLiveData<DataOrException<User, Exception>> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {

        MutableLiveData<DataOrException<User, Exception>> dataOrExceptionMutableLiveData = new MutableLiveData<>();

        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            DataOrException<User, Exception> dataOrException = new DataOrException<>();
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    User user = getUser(firebaseUser);
                    dataOrException.data = user;
                }
            } else {
                dataOrException.exception = authTask.getException();
            }
            dataOrExceptionMutableLiveData.setValue(dataOrException);
        });
        return dataOrExceptionMutableLiveData;
    }

    private User getUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        String photoUrl = firebaseUser.getPhotoUrl().toString();
        return new User(uid, name, email, photoUrl);
    }
    

    public Flowable<AuthResource<User>> firebaseSignInWithGoogleRX(AuthCredential googleAuthCredential) {
        return Single.create(new SingleOnSubscribe<AuthResource<User>>() {
            @Override
            public void subscribe(SingleEmitter<AuthResource<User>> emitter) throws Exception {
                auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            User user = getUser(firebaseUser);
                            emitter.onSuccess(AuthResource.authenticated(user));
                        }
                    } else {
                        emitter.onError(authTask.getException());
                    }
                });
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                AuthResource.error(throwable.getMessage(), null);
            }
        }).toFlowable();
    }
}
