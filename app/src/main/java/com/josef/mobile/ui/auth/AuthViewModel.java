package com.josef.mobile.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class AuthViewModel extends ViewModel {
    LiveData<DataOrException<User, Exception>> authenticatedUserLiveData;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AuthRepository authRepository;

    @Inject
    AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData =
                authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

}