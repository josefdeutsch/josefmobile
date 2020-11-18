package com.josef.mobile.ui.auth.email;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppEmailLogin implements EmailLogin {

    private static final String TAG = "AppEmailLogin";
    private final FirebaseAuth firebaseAuth;

    @Inject
    public AppEmailLogin(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Flowable<AuthResource<User>> authenticateWithEmailAccount(String email, String password) {

        Single<User> completion = signInWithEmailAndPassword(email, password);

        Single<User> verification = verifyIsEmailSigned();

        return concatOptionsSingles(completion, verification)

                .onErrorReturn(throwable -> {
                    Log.e(TAG, "apply: " + throwable.toString());
                    User user = new User();
                    user.setId(-1);
                    user.setThrowable(throwable);
                    return user;
                })

                .map((Function<User, AuthResource<User>>) user -> {
                    if (user.getId() == -1) {
                        return AuthResource.error(user.getThrowable().getMessage(), null);
                    }
                    return AuthResource.authenticated(user);
                })

                .subscribeOn(Schedulers.io());
    }

    private Flowable<User> concatOptionsSingles(Single<User> completion, Single<User> verification) {
        return completion.flatMap(verify -> verification).toFlowable();
    }

    private final User user = new User();

    private Single<User> signInWithEmailAndPassword(String email, String password) {
        return Single.create(emitter -> firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.uid = task.getResult().getUser().getUid();
                    emitter.onSuccess(user);
                } else {
                    emitter.onError(task.getException());
                }
            }
        }));
    }

    private Single<User> verifyIsEmailSigned() {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                emitter.onSuccess(user);
            } else {
                emitter.onError(new RuntimeException("We have sent you an email, please click on the link below " +
                        "and activate your account.."));
            }
        });
    }
}
