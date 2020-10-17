package com.josef.mobile.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.josef.mobile.ui.intro.AuthResource;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MediatorLiveData<AuthResource<User>> posts;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public MediatorLiveData<AuthResource<User>> getPosts() {
        return posts;
    }

    void signInWithGoogle(AuthCredential googleAuthCredential) {

        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(AuthResource.loading(null));

            Flowable<AuthResource<User>> flowable = authRepository.firebaseSignInWithGoogleRX(googleAuthCredential);
            flowable.subscribe();
            compositeDisposable.add(flowable.subscribe());

            final LiveData<AuthResource<User>> source = LiveDataReactiveStreams.fromPublisher(
                    flowable.subscribeOn(Schedulers.io()));
            posts.addSource(source, new Observer<AuthResource<User>>() {

                @Override
                public void onChanged(AuthResource<User> userAuthResource) {
                    posts.setValue(userAuthResource);
                    posts.removeSource(source);
                }
            });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }
}