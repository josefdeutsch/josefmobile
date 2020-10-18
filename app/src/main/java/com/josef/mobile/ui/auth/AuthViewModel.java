package com.josef.mobile.ui.auth;

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


    public Single<AuthCredential> getGoogleSignInAccoutn(Task<GoogleSignInAccount> task) {
        return Single.create(emitter -> {
            try {
                GoogleSignInAccount signed = task.getResult(ApiException.class);
                if (signed != null) {
                    String googleTokenId = signed.getIdToken();
                    AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
                    emitter.onSuccess(googleAuthCredential);
                } else {
                    emitter.onError(new NullPointerException("googleAuthCredential is null"));
                }
            } catch (ApiException e) {
                emitter.onError(e);
            }
        });
    }

    public Single<AuthResource<User>> getAuthResource(AuthCredential googleAuthCredential) {
        return Single.create(emitter -> {
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

        });
    }


    public Flowable<AuthResource<User>> getFlowable(Task<GoogleSignInAccount> googleSignInAccountSingle) {

        Flowable<AuthResource<User>> flowable = getGoogleSignInAccoutn(googleSignInAccountSingle)
                .flatMap(authCredential -> getAuthResource(authCredential))
                .doOnError(throwable -> AuthResource.error(throwable.getMessage(), null))
                .toFlowable();

        compositeDisposable.add(flowable.subscribe());

        return flowable;

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