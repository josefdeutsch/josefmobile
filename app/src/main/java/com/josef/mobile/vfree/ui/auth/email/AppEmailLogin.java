package com.josef.mobile.vfree.ui.auth.email;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class AppEmailLogin implements EmailLogin {

    @NonNull
    private final FirebaseAuth firebaseAuth;
    @NonNull
    private final Context context;

    @Inject
    public AppEmailLogin(@NonNull Context context,
                         @NonNull FirebaseAuth firebaseAuth) {

        this.context = context;
        this.firebaseAuth = firebaseAuth;
    }

    @NonNull
    public Flowable<AuthResource<User>> authenticateWithEmailAccount(@NonNull String email,
                                                                     @NonNull String password) {

        Single<User> completion = signInWithEmailAndPassword(email, password);

        Single<User> verification = verifyIsEmailSigned();

        return concatOptionsSingles(completion, verification)

                .onErrorReturn(throwable -> {
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

    @NonNull
    private Flowable<User> concatOptionsSingles(@NonNull Single<User> completion,
                                                @NonNull Single<User> verification) {

        return completion.flatMap(verify -> verification).toFlowable();
    }


    private final User user = new User(); // refactor !

    private Single<User> signInWithEmailAndPassword(@NonNull String email,
                                                    @NonNull String password) {

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

    @NonNull
    private Single<User> verifyIsEmailSigned() {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                emitter.onSuccess(user);
            } else {
                emitter.onError(new RuntimeException(context.getResources().getString(R.string.app_email_login)));
            }
        });
    }
}
