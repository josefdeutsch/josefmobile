package com.josef.mobile.ui.auth;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.josef.mobile.SessionManager;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private final FirebaseAuth auth;
    private final SessionManager sessionManager;
    private static final String TAG = "AuthViewModel";

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    AuthViewModel(SessionManager sessionManager, FirebaseAuth auth) {
        this.auth = auth;
        this.sessionManager = sessionManager;
    }

    public LiveData<AuthResource<User>> getPosts() {
        return sessionManager.getAuthUser();
    }

    void signInWithGoogle(Task<GoogleSignInAccount> googleSignInAccountSingle) {
        sessionManager.observeAuthResource(getLiveData(getFlowable(googleSignInAccountSingle)));
    }

    private LiveData<AuthResource<User>> getLiveData(Flowable<AuthResource<User>> flowable) {
        return LiveDataReactiveStreams.fromPublisher(
                flowable.subscribeOn(Schedulers.io()));
    }

    public Single<User> addOnFirebaseCompletionListener(AuthCredential googleAuthCredential) {
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


    public Flowable<AuthResource<User>> getFlowable(Task<GoogleSignInAccount> googleSignInAccountSingle) {

        Flowable<AuthResource<User>> flowable = getGoogleSignedIn(googleSignInAccountSingle)
                .flatMap(authCredential -> addOnFirebaseCompletionListener(authCredential))
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "onErrorReturn: " + throwable.getMessage());
                    User user = new User();
                    user.setId(-1);
                    return user;
                })
                .map(user -> getAuthResource(user))
                .toFlowable();

        compositeDisposable.add(flowable.subscribe());

        return flowable;

    }

    private AuthResource<User> getAuthResource(User user) {
        if (user.getId() == -1) return AuthResource.error("error", null);
        return AuthResource.authenticated(user);
    }


    public Single<AuthCredential> getGoogleSignedIn(Task<GoogleSignInAccount> task) {
        return Single.just(task)
                .map(tast -> getGoogleAccountResult(tast))
                .map(googleSignInAccount -> getAuthCredential(googleSignInAccount));
    }

    private AuthCredential getAuthCredential(GoogleSignInAccount googleSignInAccount) {
        if (googleSignInAccount != null) {
            String googleTokenId = googleSignInAccount.getIdToken();
            return GoogleAuthProvider.getCredential(googleTokenId, null);
        } else {
            throw new NullPointerException("googleAuthCredential is null");
        }
    }

    private GoogleSignInAccount getGoogleAccountResult(Task<GoogleSignInAccount> task) throws ApiException {
        return task.getResult(ApiException.class);
    }


    private User getUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        String photoUrl = firebaseUser.getPhotoUrl().toString();
        return new User(uid, name, email, photoUrl);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }
}