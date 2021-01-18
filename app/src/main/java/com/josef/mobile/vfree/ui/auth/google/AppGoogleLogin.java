package com.josef.mobile.vfree.ui.auth.google;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public final class AppGoogleLogin implements GoogleLogin {

    private static final String TAG = "AppGoogleLogin";
    private final FirebaseAuth auth;

    @Inject
    public AppGoogleLogin(FirebaseAuth auth) {
        this.auth = auth;
    }

    public Flowable<AuthResource<User>> authenticateWithGoogle(Task<GoogleSignInAccount> googleSignInAccountSingle) {

        return authenticateWithCredentials(googleSignInAccountSingle)
                .flatMap(authCredential -> addOnFirebaseCompletionListener(authCredential))
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "onErrorReturn: " + throwable.getMessage());
                    User user = new User();
                    user.setId(-1);
                    return user;
                })
                .map((Function<User, AuthResource<User>>) user -> {
                    if (user.getId() == -1) return AuthResource.error("error", null);
                    return AuthResource.authenticated(user);
                })
                .toFlowable();


    }

    @NotNull
    private Single<AuthCredential> authenticateWithCredentials(Task<GoogleSignInAccount> googleSignInAccountSingle) {
        return Single.just(googleSignInAccountSingle)
                .map(task -> task.getResult(ApiException.class))
                .map(googleSignInAccount -> {
                    if (googleSignInAccount != null) {
                        String googleTokenId = googleSignInAccount.getIdToken();
                        return GoogleAuthProvider.getCredential(googleTokenId, null);
                    } else {
                        throw new NullPointerException("googleAuthCredential is null");
                    }
                });
    }

    @NotNull
    private Single<User> addOnFirebaseCompletionListener(AuthCredential googleAuthCredential) {
        return Single.create(emitter -> {
            auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser != null) {
                        User user = getUser(firebaseUser);
                        emitter.onSuccess(user);
                    }
                } else {
                    emitter.onError(authTask.getException());
                }
            });

        });
    }


    private User getUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        String photoUrl = firebaseUser.getPhotoUrl().toString();
        return new User(uid, name, email, photoUrl);
    }
}
