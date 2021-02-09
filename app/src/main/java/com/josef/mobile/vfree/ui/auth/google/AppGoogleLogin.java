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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
    public AppGoogleLogin(
            @Nullable FirebaseAuth auth)
    {
        this.auth = auth;
    }

    @NotNull
    public Flowable<AuthResource<User>> authenticateWithGoogle(
            @NotNull Task<GoogleSignInAccount> googleSignInAccountSingle)
    {
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
    private Single<AuthCredential> authenticateWithCredentials(
            @NotNull Task<GoogleSignInAccount> googleSignInAccountSingle)
    {
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
    private Single<User> addOnFirebaseCompletionListener(
            @NotNull AuthCredential googleAuthCredential) {
        return Single.create(emitter -> {
            auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    try{
                        User user = getUser(firebaseUser);
                        emitter.onSuccess(user);
                    }catch (NullPointerException e){
                        Log.e(TAG, "addOnFirebaseCompletionListener: "+e.getMessage().toString());
                    }
                } else {
                    emitter.onError(authTask.getException());
                }
            });

        });
    }

    @Nullable
    private User getUser(@Nullable FirebaseUser firebaseUser) {
        String uid = Objects.requireNonNull(firebaseUser.getUid(), "fb ui must not be null" );
        String name = Objects.requireNonNull(firebaseUser.getDisplayName(), "fb name must not be null" );
        String email = Objects.requireNonNull(firebaseUser.getEmail(), "fb email must not be null" );
        String photoUrl = Objects.requireNonNull(firebaseUser.getPhotoUrl(), "fb photoUrl must not be null" ).toString();
        return new User(uid, name, email, photoUrl);
    }
}
